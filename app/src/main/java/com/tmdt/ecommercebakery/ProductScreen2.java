package com.tmdt.ecommercebakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.tmdt.ecommercebakery.Model.Cart;
import com.tmdt.ecommercebakery.Model.Products;
import com.tmdt.ecommercebakery.ViewHolder.ProductViewHolder;

public class ProductScreen2 extends AppCompatActivity {

    ImageButton BackButton;

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    ImageButton BtnCart;
    Button  btnCroissant, btnLightMeal, btnDessert, btnCake;
    RecyclerView.LayoutManager layoutManager;

    private EditText inputText;
    private Button SearchBtn;
    private RecyclerView searchList;
    private String SearchInput;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_screen2);

        btnCroissant = findViewById(R.id.btnCroissant);
        btnDessert = findViewById(R.id.btnDessert);
        btnLightMeal = findViewById(R.id.btnLightMeal);
        btnCake = findViewById(R.id.btnCake);



        //Admin-Maintain
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            type = getIntent().getExtras().get("Admin").toString();
        }

        //Admin-Maintain
        btnLightMeal = findViewById(R.id.btnLightMeal);
        btnLightMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductScreen2.this, ProductScreen3.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);

            }
        });


        ///
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //Admin-Maintain
        BackButton = findViewById(R.id.back);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("Admin"))
                {
                    Intent intent = new Intent(ProductScreen2.this, AdminCategory.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(ProductScreen2.this, Home.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        //Admin-Maintain
        btnCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(type.equals("Admin"))
                {
                    Intent intent = new Intent(ProductScreen2.this, ProductScreen.class);
                    intent.putExtra("Admin", "Admin");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ProductScreen2.this, ProductScreen.class);
                    startActivity(intent);
                }

            }
        });
        btnLightMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("Admin"))
                {
                    Intent intent = new Intent(ProductScreen2.this, ProductScreen3.class);
                    intent.putExtra("Admin", "Admin");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ProductScreen2.this, ProductScreen3.class);
                    startActivity(intent);
                }
            }
        });
        btnDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("Admin"))
                {
                    Intent intent = new Intent(ProductScreen2.this, ProductScreen4.class);
                    intent.putExtra("Admin", "Admin");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ProductScreen2.this, ProductScreen4.class);
                    startActivity(intent);
                }
            }
        });
        /// Search
        inputText = findViewById(R.id.search_product_name);
        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.recycler_menu);
        searchList.setLayoutManager(new LinearLayoutManager(ProductScreen2.this));

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchInput = inputText.getText().toString();
                onStart();
            }
        });//////////

        //sửa chỗ này
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        BtnCart = findViewById(R.id.cart_imageButton);

        BtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductScreen2.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products").child("Crossant");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("pname").startAt(SearchInput).endAt(SearchInput + "\uf8ff"), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price: " + model.getPrice() + "VND");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        //Admin-Maintain
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(type.equals("Admin"))
                                {
                                    Intent intent = new Intent(ProductScreen2.this, AdminMaintainProductsActivity2.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(ProductScreen2.this, ProductsDetailActivity2.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}

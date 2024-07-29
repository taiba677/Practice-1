package com.example.practice1;

import static java.lang.System.exit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.ImageView;
public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView = findViewById(R.id.imageView);

        Glide.with(this).load(R.drawable.cook).into(imageView);
        auth=FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);


        //   loadFragment(new AFragment());

        // } else if (id == R.id.optservi) {
        //      Toast.makeText(getApplicationContext(),"Sevi",Toast.LENGTH_SHORT).show();
        //  } else if (id == R.id.optsett) {
        //      Toast.makeText(getApplicationContext(),"Settings opened",Toast.LENGTH_SHORT).show();
        // } else if (id == R.id.optlog) {
        //    Toast.makeText(getApplicationContext(),"Logout opened",Toast.LENGTH_SHORT).show();
        //  } else {
        //     Toast.makeText(getApplicationContext(),"nothing",Toast.LENGTH_SHORT).show();
        // }
        // drawerLayout.closeDrawer(GravityCompat.START);

        // return true;

    }

    @Override
    public void onBackPressed() {
       if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
           drawerLayout.closeDrawer(GravityCompat.START);
       }else {
           super.onBackPressed();
       }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, fragment);
        ft.commit();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.opthome:
               loadFragment(new AFragment());
                break;
            case R.id.optservi:
                Intent intent=new Intent(this,services.class);
                startActivity(intent);
            case R.id.optlog:
                auth.signOut();
                startActivity(new Intent(home.this,LoginActivity.class));
                finish();
                break;
            case R.id.optsett:
                Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    FirebaseAuth auth;
}

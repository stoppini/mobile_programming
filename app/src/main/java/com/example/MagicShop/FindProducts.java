package com.example.MagicShop;import androidx.appcompat.app.AppCompatActivity;import android.content.Intent;import android.os.Bundle;import android.util.Log;import android.view.View;import android.widget.AdapterView;import android.widget.ArrayAdapter;import android.widget.EditText;import android.widget.Spinner;import android.widget.Toast;import com.example.MagicShop.model.DatabaseAccess;import com.example.MagicShop.model.Product;import java.io.Serializable;import java.util.ArrayList;import java.util.List;public class FindProducts extends AppCompatActivity implements AdapterView.OnItemSelectedListener{    DatabaseAccess dbA;    private EditText text_name;    private Spinner spin_exp;    private Spinner spin_rarity;    private Spinner spin_type;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_find_products);        text_name = (EditText) findViewById(R.id.product_name);        spin_exp = (Spinner) findViewById(R.id.spinner_expansion);        spin_rarity = (Spinner) findViewById(R.id.spinner_rarity);        spin_type = (Spinner) findViewById(R.id.spinner_type);        //Getting the instance of Spinner and applying OnItemSelectedListener on it        spin_exp.setOnItemSelectedListener(this);        spin_rarity.setOnItemSelectedListener(this);        spin_type.setOnItemSelectedListener(this);        dbA = DatabaseAccess.getDb();        List<String> expansions = dbA.getAllExpansion(getString(R.string.all));        List<String> raritys = dbA.getAllRaritys(getString(R.string.all));        List<String> types = dbA.getAllTypes(getString(R.string.all));        Log.println(Log.DEBUG,"DB",expansions.toString());        //Creating the ArrayAdapter instance having the bank name list        ArrayAdapter adap_exp = new ArrayAdapter(this,android.R.layout.simple_spinner_item,expansions);        adap_exp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        //Setting the ArrayAdapter data on the Spinner        spin_exp.setAdapter(adap_exp);        //Creating the ArrayAdapter instance having the bank name list        ArrayAdapter adap_rarity = new ArrayAdapter(this,android.R.layout.simple_spinner_item,raritys);        adap_rarity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        //Setting the ArrayAdapter data on the Spinner        spin_rarity.setAdapter(adap_rarity);        //Creating the ArrayAdapter instance having the bank name list        ArrayAdapter adap_type = new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);        adap_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        //Setting the ArrayAdapter data on the Spinner        spin_type.setAdapter(adap_type);    }    public void doSearch(View loginButton) {        String name = text_name.getText().toString();        String expansion = spin_exp.getSelectedItem().toString();        String rarity = spin_rarity.getSelectedItem().toString();        String type = spin_type.getSelectedItem().toString();        List<Product> products = dbA.getSearchProducts(name, expansion, rarity, type);        if (products.size() == 0) {            Toast.makeText(this.getBaseContext().getApplicationContext(),                    R.string.no_products_on_search, Toast.LENGTH_SHORT).show();        } else {            final Intent showProductIntent = new Intent(FindProducts.this, ShowProducts.class);            List<Long> ids = new ArrayList<>();            for (Product p : products) {                ids.add(p.getId());            }            showProductIntent.putExtra(Product.PRODUCT_LIST_EXTRA, (Serializable) ids);            startActivity(showProductIntent);        }    }    //Performing action onItemSelected and onNothing selected    @Override    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {        // TODO Auto-generated method stub    }    @Override    public void onNothingSelected(AdapterView<?> arg0) {        // TODO Auto-generated method stub    }}
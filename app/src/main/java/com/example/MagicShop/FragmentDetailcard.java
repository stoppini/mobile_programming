package com.example.MagicShop;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.example.MagicShop.model.ProductOnSale;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;


public class FragmentDetailcard extends Fragment {

    private DatabaseAccess dbA;
    private TextView nameText;
    private TextView expansionText;
    private TextView rarityText;
    private TextView ruleText;
    private ImageView imageCard;
    private List<ProductOnSale> listProductOnSale = new LinkedList<>();
    private User userLogged;
    private Product product;
    private OnNavigationListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_detailcard, container, false);

        nameText = (TextView) view.findViewById(R.id.name_view);
        expansionText = (TextView) view.findViewById(R.id.expansion_view);
        rarityText = (TextView) view.findViewById(R.id.rarity_view);
        ruleText = (TextView) view.findViewById(R.id.rule_view);
        imageCard = (ImageView) view.findViewById(R.id.image_view);

        final Button sellButton = (Button) view.findViewById(R.id.sell_product);

        if (!PreferenceUtils.isLogged(getContext())){
            sellButton.setVisibility(View.GONE);
        }


        dbA = DatabaseAccess.getDb();
        product = dbA.getProductFromId((Long)getActivity().getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA));

        if(PreferenceUtils.getId(getActivity().getApplicationContext())!=null){
            userLogged = dbA.getUserFromId(PreferenceUtils.getId(getActivity().getApplicationContext()));
            sellButton.setVisibility(View.VISIBLE);
            sellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSellDialog(getActivity(), product, userLogged);
                    listener= (OnNavigationListener) getActivity();
                }
            });
        }

        if (!getResources().getBoolean(R.bool.dual_pane)){
            //Abilito il bottonne con l'evento per passare all'activity con il singolo fragment
            final Button gotoBuy = (Button) view.findViewById(R.id.buy_product_detail);
            gotoBuy.setVisibility(View.VISIBLE);
            gotoBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener = (OnNavigationListener) getActivity();
                    listener.onButtonSeller();

                }
            });

        }else{
            final Button gotoBuy = (Button) view.findViewById(R.id.buy_product_detail);
            gotoBuy.setVisibility(View.GONE);
        }

        nameText.setText(product.getName());
        expansionText.setText(product.getExpansion());
        rarityText.setText(product.getRarity());
        ruleText.setText(product.getRule());
        Picasso.get().load(""+product.getImg()).into(imageCard);

        return view;
    }

    private void showSellDialog(Context c, Product p, User u) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(getString(R.string.title_price)+"\n"+p.getName())
                .setMessage((getString(R.string.message_price)))
                .setView(taskEditText)
                .setPositiveButton(getString(R.string.sell_price), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Float price = null;
                        try{
                            price = Float.parseFloat(String.valueOf(taskEditText.getText()));
                            DecimalFormat df = new DecimalFormat("#.##");
                            price = Float.parseFloat(df.format(price));
                            if(price >= 0.01){
                                dbA.sellProductFromUser(p, u, price);
                                onStart();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_price) , Toast.LENGTH_SHORT).show();
                                showSellDialog(c,p,u);
                            }

                        } catch (Exception ex){
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_price), Toast.LENGTH_SHORT).show();
                            showSellDialog(c,p,u);
                        }
                        //aggiorna dopo aver creato un nuovo elemento
                        listener.newSell();
                    }
                })
                .setNegativeButton(getString(R.string.undo_price), null)
                .create();
        dialog.show();
    }
}
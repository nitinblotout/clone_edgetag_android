package com.edgetag.sampleapp.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.edgetag.EdgeTag;
import com.edgetag.sampleapp.R;
import com.edgetag.sampleapp.domain.helper.Connectivity;
import com.edgetag.sampleapp.domain.mining.AprioriFrequentItemsetGenerator;
import com.edgetag.sampleapp.domain.mining.FrequentItemsetData;
import com.edgetag.sampleapp.model.CenterRepository;
import com.edgetag.sampleapp.model.entities.Money;
import com.edgetag.sampleapp.model.entities.Product;
import com.edgetag.sampleapp.util.PreferenceHelper;
import com.edgetag.sampleapp.util.TinyDB;
import com.edgetag.sampleapp.util.Utils;
import com.edgetag.sampleapp.util.Utils.AnimationType;
import com.edgetag.sampleapp.view.fragment.HomeFragment;
import com.edgetag.model.CompletionHandler;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ECartHomeActivity extends AppCompatActivity {

    public static final double MINIMUM_SUPPORT = 0.1;
    private static final String TAG = ECartHomeActivity.class.getSimpleName();
    AprioriFrequentItemsetGenerator<String> generator =
            new AprioriFrequentItemsetGenerator<>();
    private int itemCount = 0;
    private BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    private DrawerLayout mDrawerLayout;

    private TextView checkOutAmount, itemCountTextView;
    private AVLoadingIndicatorView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecart);

        CenterRepository.getCenterRepository().setListOfProductsInShoppingList(
                new TinyDB(getApplicationContext()).getListObject(
                        PreferenceHelper.MY_CART_LIST_LOCAL, Product.class));

        itemCount = CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                .size();

        //	makeFakeVolleyJsonArrayRequest();

        TextView offerBanner = ((TextView) findViewById(R.id.new_offers_banner));

        itemCountTextView = (TextView) findViewById(R.id.item_count);
        itemCountTextView.setSelected(true);
        itemCountTextView.setText(String.valueOf(itemCount));

        checkOutAmount = (TextView) findViewById(R.id.checkout_amount);
        checkOutAmount.setSelected(true);
        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());
        offerBanner.setSelected(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        progressBar = (AVLoadingIndicatorView) findViewById(R.id.loading_bar);

        checkOutAmount.setOnClickListener(v -> {

            Utils.vibrate(getApplicationContext());

            Utils.switchContent(R.id.frag_container,
                    Utils.SHOPPING_LIST_TAG, ECartHomeActivity.this,
                    AnimationType.SLIDE_UP);

        });


        if (itemCount != 0) {
            for (Product product : CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList()) {

                updateCheckOutAmount(
                        BigDecimal.valueOf(Long.parseLong(product.getSellMRP())),
                        true);
            }
        }

        findViewById(R.id.item_counter).setOnClickListener(
                v -> {

                    Utils.vibrate(getApplicationContext());
                    Utils.switchContent(R.id.frag_container,
                            Utils.SHOPPING_LIST_TAG,
                            ECartHomeActivity.this, AnimationType.SLIDE_UP);

                });

        findViewById(R.id.checkout).setOnClickListener(
                v -> {

                    Utils.vibrate(getApplicationContext());

                    showPurchaseDialog();

                });

        Utils.switchFragmentWithAnimation(R.id.frag_container,
                new HomeFragment(), this, Utils.HOME_FRAGMENT,
                AnimationType.SLIDE_UP);

        toggleBannerVisibility();

        mNavigationView
                .setNavigationItemSelectedListener(menuItem -> {

                    menuItem.setChecked(true);
                    switch (menuItem.getItemId()) {
                        case R.id.home:

                            mDrawerLayout.closeDrawers();

                            Utils.switchContent(R.id.frag_container,
                                    Utils.HOME_FRAGMENT,
                                    ECartHomeActivity.this,
                                    AnimationType.SLIDE_LEFT);

                            return true;

                        case R.id.my_cart:

                            mDrawerLayout.closeDrawers();

                            Utils.switchContent(R.id.frag_container,
                                    Utils.SHOPPING_LIST_TAG,
                                    ECartHomeActivity.this,
                                    AnimationType.SLIDE_LEFT);
                            return true;

                        case R.id.apriori_result:

                            mDrawerLayout.closeDrawers();

                            startActivity(new Intent(ECartHomeActivity.this, APrioriResultActivity.class));

                            return true;
                        default:
                            return true;
                    }
                });



        EdgeTag.INSTANCE.tag("persona",null,null, new CompletionHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {

            }
        });

    }

    public AVLoadingIndicatorView getProgressBar() {
        return progressBar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateItemCount(boolean ifIncrement) {
        if (ifIncrement) {
            itemCount++;
            itemCountTextView.setText(String.valueOf(itemCount));

        } else {
            itemCountTextView.setText(String.valueOf(itemCount <= 0 ? 0
                    : --itemCount));
        }

        toggleBannerVisibility();
    }

    public void updateCheckOutAmount(BigDecimal amount, boolean increment) {

        if (increment) {
            checkoutAmount = checkoutAmount.add(amount);
        } else {
            if (checkoutAmount.signum() == 1)
                checkoutAmount = checkoutAmount.subtract(amount);
        }

        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Store Shopping Cart in DB
        new TinyDB(getApplicationContext()).putListObject(
                PreferenceHelper.MY_CART_LIST_LOCAL, CenterRepository
                        .getCenterRepository().getListOfProductsInShoppingList());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Show Offline Error Message
        if (!Connectivity.isConnected(getApplicationContext())) {
            final Dialog dialog = new Dialog(ECartHomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.connection_dialog);
            Button dialogButton = (Button) dialog
                    .findViewById(R.id.dialogButtonOK);

            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
        }

    }

    /*
     * Toggles Between Offer Banner and Checkout Amount. If Cart is Empty SHow
     * Banner else display total amount and item count
     */
    public void toggleBannerVisibility() {
        if (itemCount == 0) {

            findViewById(R.id.checkout_item_root).setVisibility(View.GONE);
            findViewById(R.id.new_offers_banner).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.checkout_item_root).setVisibility(View.VISIBLE);
            findViewById(R.id.new_offers_banner).setVisibility(View.GONE);
        }
    }

    /*
     * get total checkout amount
     */
    public BigDecimal getCheckoutAmount() {
        return checkoutAmount;
    }



    /*
     * Get Number of items in cart
     */
    public int getItemCount() {
        return itemCount;
    }

    /*
     * Get Navigation drawer
     */
    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }


    public void showPurchaseDialog() {

        AlertDialog.Builder exitScreenDialog = new AlertDialog.Builder(ECartHomeActivity.this, R.style.PauseDialog);

        exitScreenDialog.setTitle("Order Confirmation")
                .setMessage("Would you like to place this order ?");
        exitScreenDialog.setCancelable(true);

        exitScreenDialog.setPositiveButton(
                "Place Order",
                (dialog, id) -> {
                    //finish();
                    dialog.cancel();

                    ArrayList<String> productId = new ArrayList<String>();

                    for (Product productFromShoppingList : CenterRepository.getCenterRepository().getListOfProductsInShoppingList()) {

                        //add product ids to array
                        productId.add(productFromShoppingList.getProductId());
                        EdgeTag.INSTANCE.tag("transactionData",null,null, new CompletionHandler() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(int code, String msg) {

                            }
                        });
                    }

                    //pass product id array to Apriori ALGO
                    CenterRepository.getCenterRepository()
                            .addToItemSetList(new HashSet<>(productId));

                    //Do Minning
                    FrequentItemsetData<String> data = generator.generate(
                            CenterRepository.getCenterRepository().getItemSetList()
                            , MINIMUM_SUPPORT);

                    for (Set<String> itemset : data.getFrequentItemsetList()) {
                        Log.e("APriori", "Item Set : " +
                                itemset + "Support : " +
                                data.getSupport(itemset));
                    }

                    //clear all list item
                    CenterRepository.getCenterRepository().getListOfProductsInShoppingList().clear();

                    toggleBannerVisibility();

                    itemCount = 0;
                    itemCountTextView.setText(String.valueOf(0));
                    checkoutAmount = new BigDecimal(BigInteger.ZERO);
                    checkOutAmount.setText(Money.rupees(checkoutAmount).toString());

                });

        exitScreenDialog.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.cancel());

        exitScreenDialog.setOnDismissListener(dialog -> {
            HashMap<String,Object> eventInfo = new HashMap<>();
            eventInfo.put("Order status","Order Placed Successfully");
            Snackbar.make(ECartHomeActivity.this.getWindow().getDecorView().findViewById(android.R.id.content)
                    , "Order Placed Successfully, Happy Shopping !!", Snackbar.LENGTH_LONG)
                    .setAction("View Apriori Output", view -> startActivity(new Intent(ECartHomeActivity.this, APrioriResultActivity.class))).show();
        });

        AlertDialog alert11 = exitScreenDialog.create();
        alert11.show();

    }

}

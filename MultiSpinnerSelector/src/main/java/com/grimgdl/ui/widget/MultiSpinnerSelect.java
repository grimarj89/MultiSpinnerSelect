package com.grimgdl.ui.widget;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import android.content.LocusId;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Spinner;


import com.grimgdl.R;
import com.grimgdl.adapter.ItemsAdapter;
import com.grimgdl.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MultiSpinnerSelect extends Spinner implements
         OnCancelListener {

    private String defaultText;

    private ItemsAdapter arrayAdapter;

    List<Product> list;

    private final Context _context;




    public MultiSpinnerSelect(Context context) {
        super(context);
        this._context = context;
        //setBackgroundResource(R.drawable.blue_outline);
    }

    public MultiSpinnerSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        //setBackgroundResource(R.drawable.blue_outline);
    }

    public MultiSpinnerSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;

        //setBackgroundResource(R.drawable.blue_outline);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

        List<Product> checkProducts = arrayAdapter.getProductos();
        String spinnerText;

        int checkSize = checkProducts.size();

        if (checkSize > 0 ){

            if (checkSize == 1){
                spinnerText = "1 item selected";
            }else {
                spinnerText = "some items selected";
            }

        }else {
            spinnerText = defaultText;

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(_context,
                R.layout.textview1, new String[]{ spinnerText });
        setAdapter(adapter);


    }

    @Override
    public boolean performClick() {

        super.performClick();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        arrayAdapter = new ItemsAdapter(getContext(), this.list);

        builder.setAdapter(arrayAdapter, null);
        builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.cancel());
        builder.setOnCancelListener(this);

        AlertDialog alertDialog = builder.create();


        ListView listView = alertDialog.getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Product producto = (Product) parent.getItemAtPosition(position);

            producto.setChecked(!producto.isChecked());

            arrayAdapter.notifyDataSetChanged();

        });

        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return true;
    }

    public void setItems(List<Product> items, String allText){

        this.defaultText = allText;
        this.list = items;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(_context,
                R.layout.textview1, new String[]{ defaultText });
        setAdapter(adapter);

    }


    public interface MultiSpinnerListener {
        void onItemsSelected(boolean[] selected);
    }

    public List<Product> getSelectedItems(){
        List<Product> items = arrayAdapter.getProductos();

        return items.stream().filter(Product::isChecked).collect(Collectors.toList());
    }



}

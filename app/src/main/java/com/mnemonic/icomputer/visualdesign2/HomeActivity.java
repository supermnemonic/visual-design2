package com.mnemonic.icomputer.visualdesign2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    public final static String EXTRA_PERSON_NAME = "com.mnemonic.icomputer.screeningtest.PERSON_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView addPhoto = (ImageView)findViewById(R.id.add_photo);
        Bitmap addPhotoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.btn_add_photo);
        RoundedBitmap roundedBitmap = new RoundedBitmap(getResources());
        RoundedBitmapDrawable roundedAddPhotoBitmap = roundedBitmap.create(addPhotoBitmap, 4);
        addPhoto.setImageDrawable(roundedAddPhotoBitmap);
    }

    public void onSubmitButtonClicked(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_person_name);
        String personName = editText.getText().toString();

        if (isPalindrome(personName))
            showDialogOnSubmit("is palindrome");
        else
            showDialogOnSubmit("not palindrome");
    }

    private void showDialogOnSubmit(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent(HomeActivity.this, MainScreenActivity.class);
                        EditText editText = (EditText) findViewById(R.id.edit_person_name);
                        String personName = editText.getText().toString();
                        intent.putExtra(EXTRA_PERSON_NAME, personName);
                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }

    private boolean isPalindrome(String str) {
        str = str.replaceAll("\\s","");
        int mid = str.length()/2;
        int last = str.length() - 1;
        int j = last;
        for (int i=0;i<mid;i++) {
            if (str.charAt(i) != str.charAt(j))
                return false;
            j--;
        }
        return true;
    }

}

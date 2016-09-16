package com.rbt.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dropbox.chooser.android.DbxChooser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String APP_KEY = "iowmq12ar191eve";/* This is for you to fill in! */

    static final int DBX_CHOOSER_REQUEST = 0;  // You can change this if needed

    private DbxChooser mChooser;
    private TextView fileTV, fileSizeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChooser = new DbxChooser(APP_KEY);
        fileTV = (TextView) findViewById(R.id.fileTV);
        fileSizeTV = (TextView) findViewById(R.id.fileSizeTV);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.driveBtn:
                Intent intent = new Intent(this, DriveActivity.class);
                startActivity(intent);
                break;
            case R.id.dropBoxBtn:
                /*DbxChooser.ResultType resultType;
                switch (((RadioGroup) findViewById(R.id.link_type)).getCheckedRadioButtonId()) {
                    case R.id.link_type_direct: resultType = DbxChooser.ResultType.DIRECT_LINK; break;
                    case R.id.link_type_content: resultType = DbxChooser.ResultType.FILE_CONTENT; break;
                    case R.id.link_type_preview: resultType = DbxChooser.ResultType.PREVIEW_LINK; break;
                    default: throw new RuntimeException("unexpected link type!!");
                }
                mChooser.forResultType(resultType)
                        .launch(MainActivity.this, DBX_CHOOSER_REQUEST);*/
                DbxChooser.ResultType resultType;
                resultType = DbxChooser.ResultType.FILE_CONTENT;
                mChooser.forResultType(resultType).launch(MainActivity.this, DBX_CHOOSER_REQUEST);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                Log.d("main", "Link to selected file: " + result.getLink());

                showLink(fileTV, result.getLink());
                fileSizeTV.setText(String.valueOf(result.getSize() / 1024) + " KB", TextView.BufferType.NORMAL);

            } else {
                // Failed or was cancelled by the user.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showLink(TextView textView, Uri uri) {
        if (uri == null) {
            textView.setText("", TextView.BufferType.NORMAL);
            return;
        }
        textView.setText(uri.toString(), TextView.BufferType.NORMAL);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                Log.d("main", "Link to selected file: " + result.getLink());

                showLink(R.id.uri, result.getLink());
                ((TextView) findViewById(R.id.filename)).setText(result.getName().toString(), TextView.BufferType.NORMAL);
                ((TextView) findViewById(R.id.size)).setText(String.valueOf(result.getSize()), TextView.BufferType.NORMAL);
                showLink(R.id.icon, result.getIcon());

                Map<String, Uri> thumbs = result.getThumbnails();
                showLink(R.id.thumb64, thumbs.get("64x64"));
                showLink(R.id.thumb200, thumbs.get("200x200"));
                showLink(R.id.thumb640, thumbs.get("640x480"));
            } else {
                // Failed or was cancelled by the user.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showLink(int id, Uri uri) {
        TextView v = (TextView) findViewById(id);
        if (uri == null) {
            v.setText("", TextView.BufferType.NORMAL);
            return;
        }
        v.setText(uri.toString(), TextView.BufferType.NORMAL);
        v.setMovementMethod(LinkMovementMethod.getInstance());
    }*/
}

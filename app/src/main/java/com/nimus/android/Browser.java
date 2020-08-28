package com.nimus.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Browser extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    ProgressBar progressBar;
    private String URL;
    private int code;
    private FloatingActionButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        URL = getIntent().getStringExtra("URL");
        code = getIntent().getIntExtra("code",0);

        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBrowser);
        back = findViewById(R.id.fab_browser);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav(code);
            }
        });

        webView.setWebChromeClient(new myWebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Toast.makeText(this, "URl = "+URL, Toast.LENGTH_SHORT).show();
        webView.loadUrl(URL);
    }

    public class myWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        public void setValue(int progress){
            progressBar.setProgress(progress);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        URL= null;
        code = 0;
    }

    void Nav(int code){
        Intent intent;
        if(code == 0){
            intent = new Intent(Browser.this, MainActivity.class);
            startActivity(intent);
        }
        if(code == 1){
            intent = new Intent(Browser.this, projectDesc.class);
            startActivity(intent);
        }


    }
}

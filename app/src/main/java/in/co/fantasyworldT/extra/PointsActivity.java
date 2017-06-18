package in.co.fantasyworldT.extra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by C5245675 on 6/10/2017.
 */

public class PointsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView mWebView=new WebView(PointsActivity.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        String pdf = "http://www.adobe.com/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
        mWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+pdf);
        setContentView(mWebView);
    }
}

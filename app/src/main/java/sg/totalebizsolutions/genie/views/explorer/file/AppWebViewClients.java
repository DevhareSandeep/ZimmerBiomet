package sg.totalebizsolutions.genie.views.explorer.file;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Sandeep Devhare @APAR on 4/10/2017.
 */

public class AppWebViewClients extends WebViewClient {



    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);

    }
}
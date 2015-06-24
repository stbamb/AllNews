package com.stbam.allnews.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;

import com.stbam.allnews.R;
import com.stbam.allnews.parser.RSSFeed;

public class DetailFragment extends Fragment {

    private int fPos;
    RSSFeed fFeed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fFeed = (RSSFeed)getArguments().getSerializable("feed");
        fPos = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        TextView title = (TextView)view.findViewById(R.id.title);
        WebView desc = (WebView)view.findViewById(R.id.desc);
        ScrollView sv = (ScrollView)view.findViewById(R.id.sv);
        sv.setVerticalFadingEdgeEnabled(true);


        // todas las propiedades del WebView
        WebSettings ws = desc.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(false);
        ws.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        ws.setUseWideViewPort(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(true);
        ws.setTextZoom(250);
        ws.setPluginState(PluginState.ON);
        ws.setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
        desc.setWebViewClient(new WebViewClient());


        // se acomoda el cuerpo de la noticia en el WebView
        title.setText(fFeed.getItem(fPos).getTitle());

        System.out.println(fFeed.getItem(fPos).getDescription());
        String data = "<html><body><center>" + fFeed.getItem(fPos).getDescription() + "</center></body></html>";
        desc.loadData(data, "text/html; charset=UTF-8", null);


        return view;
    }
}

package com.project.senior.historyexplorer;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MediaActivity extends Activity{

    private ActionBar actionBar;
    private ActionBar.TabListener tabListener;

    private static final String url = "http://ha-dev.cis.fiu.edu/WebApp/Files/data.xml";

    //XML Node Keys
    private static final String KEY_ITEM = "item"; // parent node
    private static final String KEY_NAME = "name";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LONGIT = "longitude";
    private static final String KEY_DESC = "description";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_AUDIO = "audio";
    private static final String KEY_DOCU ="document";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_menu);


        actionBar = getActionBar();
        //handles Action Bar Design: icon transparent and green
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003300")));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        /*ArrayList<HashMap<String, String>> menuItems = new ArrayList<>();
        //xmlParser parser = new XMLParser();
        String xml = (String) new GetXmlFromUrl(url);
        Document doc = getDomElement(xml);

        NodeList n1 = doc.getElementsByTagName(KEY_ITEM);
        // creating new HashMap
        HashMap<String, String> map = new HashMap<>();

        menuItems.add(xmlHandler(n1, map));*/


        // Create a tab listener that is called when the user changes tabs.
        tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                if (tab.getText() == "Audio")
                {
                    //createList();
                }

            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        //Label's Tabs
        createTabs();

        //setHasOptionsMenu(true);
    }

    /*This needs to be changed to add the titles based on the strings/media_split_action_bar*/
    public void createTabs()
    {
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Audio")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Photos")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Documents")
                        .setTabListener(tabListener));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003300")));
    }

    /*public HashMap<String, String> xmlHandler(NodeList nl, HashMap<String,String> map)
    {
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key => value

            map.put(KEY_NAME, getValue(e, KEY_NAME));
            map.put(KEY_LAT, getValue(e, KEY_LAT));
            map.put(KEY_LONGIT, getValue(e, KEY_LONGIT));
            map.put(KEY_DESC, getValue(e, KEY_DESC));
            map.put(KEY_PHOTO, getValue(e, KEY_PHOTO));
            map.put(KEY_AUDIO, getValue(e, KEY_AUDIO));
            map.put(KEY_DOCU, getValue(e, KEY_DOCU));
        }
        return map;
    }
*/
    //This method will be used to generate the information within the Media page
    public void createList()
    {
        /*// Adding menuItems to ListView
                    ListAdapter adapter = new SimpleAdapter(this, menuItems,
                            R.layout.list_item,
                            new String[] { KEY_NAME, KEY_DESC, KEY_COST }, new int[] {
                            R.id.name, R.id.desciption, R.id.cost });

                    setListAdapter(adapter);

                    // selecting single ListView item
                    ListView lv = getListView();
                    // listening to single listitem click
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // getting values from selected ListItem
                            String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                            String cost = ((TextView) view.findViewById(R.id.cost)).getText().toString();
                            String description = ((TextView) view.findViewById(R.id.desciption)).getText().toString();

                            // Starting new intent
                            Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
                            in.putExtra(KEY_NAME, name);
                            in.putExtra(KEY_COST, cost);
                            in.putExtra(KEY_DESC, description);
                            startActivity(in);

                        }
                    });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


     //Action Bar Icons activity handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.home_icon:
                Intent intent = new Intent(MediaActivity.this, MainMenu.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public class GetXmlFromUrl extends AsyncTask<Void, Void, String > {

        *//*private String toSearch;
        private Address address;*//*
        private String url;

        public GetXmlFromUrl(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
           // public String getXmlFromUrl(String url) {
                String xml = null;

                try {
                    // defaultHttpClient
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    xml = EntityUtils.toString(httpEntity);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // return XML
                return xml;
            //}
        }

      *//*  protected void onPostExecute(RSSFeed feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }*//*
    }

    public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }*/
}

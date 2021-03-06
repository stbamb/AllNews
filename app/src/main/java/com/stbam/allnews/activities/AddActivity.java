package com.stbam.allnews.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.stbam.allnews.R;
import com.stbam.allnews.parser.FeedSource;
import com.stbam.allnews.widgets.AnimatedExpandableListView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends Activity
{
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    public static ArrayList<FeedSource> feedLink;
    SplashActivity s = new SplashActivity();
    private static ArrayList<String> lista_categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final List<GroupItem> items = new ArrayList<GroupItem>();

        feedLink = new SplashActivity().lista_sources2;
        lista_categorias = obtenerCategorias();
        int cant_categorias = lista_categorias.size();

        llenarInfo(items, cant_categorias);

        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listView2);
        listView.setVerticalFadingEdgeEnabled(true);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }

                return true;
            }

        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                // encuentra la posicion del item seleccionado
                // y le pone o le quita el check

                ImageView checked_item = (ImageView) v.findViewById(R.id.check);

                ChildItem item = items.get(groupPosition).items.get(childPosition);

                int pos = getPosicion(item.title);

                if (checked_item.getVisibility() == View.VISIBLE)
                {
                    checked_item.setVisibility(View.INVISIBLE);
                    feedLink.get(pos).setAceptado(false);
                    escribirRegistro("RSSReaderLog.stb");
                    escribirRegistro("RSSReaderFeed.stb");
                }
                else
                {
                    checked_item.setVisibility(View.VISIBLE);
                    feedLink.get(pos).setAceptado(true);
                    escribirRegistro("RSSReaderLog.stb");
                    escribirRegistro("RSSReaderFeed.stb");
                }

                return true;
            }
        });
    }

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
    }

    private static class ChildHolder {
        TextView title;
        ImageView isChecked;
    }

    private static class GroupHolder {
        TextView title;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; finish activity to go home
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.feed_source, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.source_name);
                //holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                holder.isChecked = (ImageView) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            //holder.hint.setText(item.hint);

            int pos = getPosicion(item.title);

            if (feedLink.get(pos).isAceptado()) {
                holder.isChecked.setVisibility(View.VISIBLE);
            }
            else if (!feedLink.get(pos).isAceptado()) {
                holder.isChecked.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

    public void escribirRegistro(String file_name) {

        FileOutputStream fOut = null;
        ObjectOutputStream osw;

        try {
            fOut = openFileOutput(file_name, MODE_PRIVATE);
            osw = new ObjectOutputStream(fOut);

            for (int i = 0; i < feedLink.size(); i++) {
                FeedSource s = feedLink.get(i);
                osw.write(s.getURL().getBytes());
                osw.write(";".getBytes());
                osw.write(s.getNombre().getBytes());
                osw.write(";".getBytes());
                osw.write(s.getCategoria().getBytes());
                osw.write(";".getBytes());
                osw.write(s.getIdioma().getBytes());
                osw.write(";".getBytes());
                osw.write(s.getURLPagina().getBytes());
                osw.write(";".getBytes());
                if (s.isAceptado())
                    osw.write("trueverdadero".getBytes());
                else
                    osw.write("falsefalso".getBytes());

                osw.write(";".getBytes());
                osw.write("\n".getBytes());
            }

            osw.flush();
        }

        catch (Exception e) {
            //e.printStackTrace();
        }

        finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    // funcion que retorna todas las categorias existentes
    public ArrayList<String> obtenerCategorias()
    {
        ArrayList<String> categorias = new ArrayList<String>();
        categorias.add("Todas las fuentes");

        for (int i = 0; i < feedLink.size(); i++)
        {
            FeedSource s = feedLink.get(i);
            if (categorias != null && !categorias.contains(s.getCategoria()))
                categorias.add(s.getCategoria());
        }
        return categorias;
    }

    // funcion que retorna todas los feed sources de cierta categoria
    public ArrayList<String> obtenerCategoriasTipo(String tipo) {
        ArrayList<String> categorias_tipo = new ArrayList<String>();

        if (tipo.equalsIgnoreCase("todas las fuentes"))
        {
            for (int i = 0; i < feedLink.size(); i++) {

                FeedSource s = feedLink.get(i);
                categorias_tipo.add(s.getNombre());
            }
        return categorias_tipo;
        }

        for (int i = 0; i < feedLink.size(); i++)
        {
            FeedSource s = feedLink.get(i);
            if (s.getCategoria().equals(tipo))
                categorias_tipo.add(s.getNombre());
        }
        return categorias_tipo;
    }

    public void llenarInfo(List<GroupItem> items, int cant_categorias)
    {
        for (int i = 0; i < cant_categorias; i++)
        {
            GroupItem item = new GroupItem();
            String nombre_categoria = lista_categorias.get(i);
            nombre_categoria = Character.toString(nombre_categoria.charAt(0)).toUpperCase() + nombre_categoria.substring(1);
            item.title = nombre_categoria;
            ArrayList<String> lista_categorias_tipo = obtenerCategoriasTipo(nombre_categoria.toLowerCase());
            int cant_categorias_tipo = lista_categorias_tipo.size();

            for (int j = 0; j < cant_categorias_tipo; j++)
            {
                ChildItem child = new ChildItem();
                String nombre_source = lista_categorias_tipo.get(j);
                child.title = nombre_source;
                item.items.add(child);
            }

            items.add(item);
        }
    }

    public int getPosicion(String nombre_source)
    {
        int pos = 0;

        for (int i = 0; i < feedLink.size(); i++)
        {
            if (feedLink.get(i).getNombre().equals(nombre_source)) {
                pos = i;
                break;
            }
        }
        return pos;
    }
}

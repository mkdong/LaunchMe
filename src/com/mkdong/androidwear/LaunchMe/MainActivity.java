package com.mkdong.androidwear.LaunchMe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ItemAdapter itemAdapter;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView gridview = (GridView) findViewById(R.id.gridView);

        itemAdapter = new ItemAdapter(this);
        gridview.setAdapter(itemAdapter);

        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        itemAdapter.add("com.mobvoi.ticwear.ios");
        for (ApplicationInfo packageInfo : packages) {
            itemAdapter.add(packageInfo.packageName);
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String pkgname = itemAdapter.getItem(position);
                Toast.makeText(MainActivity.this, "boot " + pkgname , Toast.LENGTH_SHORT).show();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pkgname);
                startActivity(launchIntent);
            }
        });
    }
    private class ItemAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private Context mContext;

        public ItemAdapter(Context c) {
            mContext = c;
            inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void add(String pkgname) {
            lists.add(pkgname);
        }
        public int getCount() {
            return lists.size();
        }

        public String getItem(int position) {
            return lists.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        private ArrayList<String> lists = new ArrayList<>();

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            String pkgname = lists.get(position);
            View view;

            view = inflater.inflate(R.layout.item, null);
            final PackageManager pm = getPackageManager();
            try {
                ImageView imageView = ((ImageView)view.findViewById(R.id.imageView));
                //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setAdjustViewBounds(true);
                Drawable icon = pm.getApplicationIcon(pkgname);
                imageView.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {

            }
            try {
                ((TextView) view.findViewById(R.id.appname)).setText(
                        pm.getApplicationLabel(
                                pm.getApplicationInfo(pkgname, PackageManager.GET_META_DATA)
                        ).toString()
                );
            } catch (PackageManager.NameNotFoundException e) {

            }
            ((TextView)view.findViewById(R.id.pkgname)).setText(pkgname);
            view.setPadding(8, 8, 8, 8);

            return view;
        }

    }
}

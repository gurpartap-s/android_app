package com.example.sweat;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CardListAdapter extends ArrayAdapter<JSONObject> {
    private final Activity context;
    private final JSONArray json_arr;

    public CardListAdapter(Activity context, JSONArray json){
        super(context, R.layout.list_item);
        //Log.e("SWEAT", "CardListAdapter const");
        this.context=context;
        this.json_arr=json;
    }
    public int getCount(){
        return json_arr.length();
    }
    public View getView(int position, View view, ViewGroup parent) {
        //Log.e("SWEAT", "CardListAdapter::getView");
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        try {
            JSONObject js_obj = json_arr.getJSONObject(position);
            TextView progText = (TextView) rowView.findViewById(R.id.prog_name);
            progText.setText(js_obj.getString("name"));
            JSONObject js_trainer = js_obj.getJSONObject("trainer");
            String text;
            text = js_trainer.getString("name");
            TextView trainerText = (TextView) rowView.findViewById(R.id.trainer_name);
            trainerText.setText("with " + text);

            text = js_trainer.getString("image_url");
            ImageView image = rowView.findViewById(R.id.img_trainer);
            Glide.with(context).load(text).into(image);

            JSONArray attrib_arr = js_obj.getJSONArray("attributes");

            LinearLayout layout_intens = (LinearLayout)rowView.findViewById(R.id.attrib_intens);
            JSONObject js_att = attrib_arr.getJSONObject(0);
            double x = js_att.optDouble("value");
            int value = (int)Math.round(x);
            for(int i=0; i<value; i++){
                ImageView sw_image = new ImageView(context);
                sw_image.setImageResource(R.drawable.sweat_drop_filled);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,8,8,8);
                sw_image.setLayoutParams(params);
                layout_intens.addView(sw_image);
            }
            x = js_att.optDouble("total");
            int y = (int)Math.round(x);
            for(int i=0; i<(y-value); i++){
                ImageView sw_image = new ImageView(context);
                sw_image.setImageResource(R.drawable.sweat_drop);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,8,8,8);
                sw_image.setLayoutParams(params);
                layout_intens.addView(sw_image);
            }
            int len = attrib_arr.length();
            LinearLayout linearLayout = (LinearLayout)rowView.findViewById(R.id.attrib_layout);
            for(int i=1; i<len; i++){
                js_att = attrib_arr.getJSONObject(i);
                TextView att_text = new TextView(context);
                att_text.setText(js_att.getString("name"));
                att_text.setTextAppearance(context, R.style.TextViewMed);
                ((LinearLayout) linearLayout).addView(att_text);

                ProgressBar progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.VISIBLE);
                x = js_att.optDouble("total");
                y = (int)Math.round(x);
                progressBar.setMax(y);
                x = js_att.optDouble("value");
                y = (int)Math.round(x);
                progressBar.setProgress(y);
                progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ((LinearLayout) linearLayout).addView(progressBar);
            }

            FlexboxLayout flexboxLayout = (FlexboxLayout) rowView.findViewById(R.id.flexbox_layout);
            flexboxLayout.setFlexDirection(FlexDirection.ROW);
            JSONArray tag_arr = js_obj.getJSONArray("tags");
            len = tag_arr.length();
            for(int i=0; i<len; i++){
                JSONObject js_tag = tag_arr.getJSONObject(i);
                TextView tag_text = new TextView(context);
                tag_text.setText(js_tag.getString("name"));
                tag_text.setTextAppearance(context, R.style.TextViewSmall);
                tag_text.setBackgroundResource(R.drawable.round_corner);
                tag_text.setPadding(8, 8, 8, 8);
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,8,8,8);
                tag_text.setLayoutParams(params);
                ((FlexboxLayout) flexboxLayout).addView(tag_text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;
    };
}



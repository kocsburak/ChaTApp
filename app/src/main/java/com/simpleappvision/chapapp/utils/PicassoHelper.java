package com.simpleappvision.chapapp.utils;


import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class PicassoHelper {
    private ImageView imageView;


    public PicassoHelper(ImageView imageView) {

        this.imageView = imageView;
    }


    public void getPhoto(String url) {
        if (!url.equals("")) {
            Picasso.get().load(url).into(imageView);
        }
    }

}

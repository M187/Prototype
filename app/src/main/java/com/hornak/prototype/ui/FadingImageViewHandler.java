package com.hornak.prototype.ui;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by michal.hornak on 12/9/2017.
 */

public class FadingImageViewHandler {

    ImageView mImageView;
    Toolbar mToolbar;
    private boolean isTitleImageShown;

    public FadingImageViewHandler(ImageView imageView, Toolbar toolbar) {
        mImageView = imageView;
        mToolbar = toolbar;
    }

    public void handleScrolling(float percentage) {
        try {
            if (percentage > 0.7f && this.isTitleImageShown) {
                fadeOutAndHideImage(mImageView);
                //mToolbar.setNavigationIcon(null);
                this.isTitleImageShown = false;
            } else if (percentage < 0.7f && !this.isTitleImageShown) {
                fadeInAndHideImage(mImageView);
                //mToolbar.setNavigationIcon(R.drawable.logo_quiz);
                this.isTitleImageShown = true;
            }
        } catch (NullPointerException e) {
            if (percentage > 0.7f) {
                fadeOutAndHideImage(mImageView);
                this.isTitleImageShown = false;
            }
        }
    }

    private void fadeOutAndHideImage(final ImageView img) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.INVISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
    }

    private void fadeInAndHideImage(final ImageView img) {
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.VISIBLE);

            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
    }
}


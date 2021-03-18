// Generated by view binder compiler. Do not edit!
package com.plataforma.crpg.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.plataforma.crpg.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final AppCompatTextView actionExampleActivity;

  @NonNull
  public final AppBarLayout appbarLayout;

  @NonNull
  public final View dropshadow;

  @NonNull
  public final MaterialButton fabOptions;

  @NonNull
  public final BottomNavigationView navView;

  @NonNull
  public final RecyclerView recyclerView;

  @NonNull
  public final FrameLayout toolbar;

  private ActivityMainBinding(@NonNull CoordinatorLayout rootView,
      @NonNull AppCompatTextView actionExampleActivity, @NonNull AppBarLayout appbarLayout,
      @NonNull View dropshadow, @NonNull MaterialButton fabOptions,
      @NonNull BottomNavigationView navView, @NonNull RecyclerView recyclerView,
      @NonNull FrameLayout toolbar) {
    this.rootView = rootView;
    this.actionExampleActivity = actionExampleActivity;
    this.appbarLayout = appbarLayout;
    this.dropshadow = dropshadow;
    this.fabOptions = fabOptions;
    this.navView = navView;
    this.recyclerView = recyclerView;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    String missingId;
    missingId: {
      AppCompatTextView actionExampleActivity = rootView.findViewById(R.id.action_example_activity);
      if (actionExampleActivity == null) {
        missingId = "actionExampleActivity";
        break missingId;
      }
      AppBarLayout appbarLayout = rootView.findViewById(R.id.appbarLayout);
      if (appbarLayout == null) {
        missingId = "appbarLayout";
        break missingId;
      }
      View dropshadow = rootView.findViewById(R.id.dropshadow);
      if (dropshadow == null) {
        missingId = "dropshadow";
        break missingId;
      }
      MaterialButton fabOptions = rootView.findViewById(R.id.fab_options);
      if (fabOptions == null) {
        missingId = "fabOptions";
        break missingId;
      }
      BottomNavigationView navView = rootView.findViewById(R.id.nav_view);
      if (navView == null) {
        missingId = "navView";
        break missingId;
      }
      RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
      if (recyclerView == null) {
        missingId = "recyclerView";
        break missingId;
      }
      FrameLayout toolbar = rootView.findViewById(R.id.toolbar);
      if (toolbar == null) {
        missingId = "toolbar";
        break missingId;
      }
      return new ActivityMainBinding((CoordinatorLayout) rootView, actionExampleActivity,
          appbarLayout, dropshadow, fabOptions, navView, recyclerView, toolbar);
    }
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
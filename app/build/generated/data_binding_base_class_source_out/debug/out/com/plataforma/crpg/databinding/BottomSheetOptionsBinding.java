// Generated by view binder compiler. Do not edit!
package com.plataforma.crpg.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import com.google.android.material.button.MaterialButton;
import com.plataforma.crpg.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class BottomSheetOptionsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton buttonApply;

  @NonNull
  public final AppCompatImageView imageToggle;

  @NonNull
  public final ItemBottomSheetLineBinding layoutLine;

  @NonNull
  public final ItemBottomSheetMarkerBinding layoutMarker;

  @NonNull
  public final NestedScrollView nestedScrollView;

  @NonNull
  public final RadioButton rbHorizontal;

  @NonNull
  public final RadioButton rbVertical;

  @NonNull
  public final RadioGroup rgOrientation;

  @NonNull
  public final AppCompatTextView textAttributesHeading;

  @NonNull
  public final AppCompatTextView textOrientationHeading;

  @NonNull
  public final ConstraintLayout viewBottomSheetAttributes;

  private BottomSheetOptionsBinding(@NonNull ConstraintLayout rootView,
      @NonNull MaterialButton buttonApply, @NonNull AppCompatImageView imageToggle,
      @NonNull ItemBottomSheetLineBinding layoutLine,
      @NonNull ItemBottomSheetMarkerBinding layoutMarker,
      @NonNull NestedScrollView nestedScrollView, @NonNull RadioButton rbHorizontal,
      @NonNull RadioButton rbVertical, @NonNull RadioGroup rgOrientation,
      @NonNull AppCompatTextView textAttributesHeading,
      @NonNull AppCompatTextView textOrientationHeading,
      @NonNull ConstraintLayout viewBottomSheetAttributes) {
    this.rootView = rootView;
    this.buttonApply = buttonApply;
    this.imageToggle = imageToggle;
    this.layoutLine = layoutLine;
    this.layoutMarker = layoutMarker;
    this.nestedScrollView = nestedScrollView;
    this.rbHorizontal = rbHorizontal;
    this.rbVertical = rbVertical;
    this.rgOrientation = rgOrientation;
    this.textAttributesHeading = textAttributesHeading;
    this.textOrientationHeading = textOrientationHeading;
    this.viewBottomSheetAttributes = viewBottomSheetAttributes;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static BottomSheetOptionsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BottomSheetOptionsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.bottom_sheet_options, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BottomSheetOptionsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    String missingId;
    missingId: {
      MaterialButton buttonApply = rootView.findViewById(R.id.button_apply);
      if (buttonApply == null) {
        missingId = "buttonApply";
        break missingId;
      }
      AppCompatImageView imageToggle = rootView.findViewById(R.id.image_toggle);
      if (imageToggle == null) {
        missingId = "imageToggle";
        break missingId;
      }
      View layoutLine = rootView.findViewById(R.id.layout_line);
      if (layoutLine == null) {
        missingId = "layoutLine";
        break missingId;
      }
      ItemBottomSheetLineBinding layoutLineBinding = ItemBottomSheetLineBinding.bind(layoutLine);
      View layoutMarker = rootView.findViewById(R.id.layout_marker);
      if (layoutMarker == null) {
        missingId = "layoutMarker";
        break missingId;
      }
      ItemBottomSheetMarkerBinding layoutMarkerBinding = ItemBottomSheetMarkerBinding.bind(layoutMarker);
      NestedScrollView nestedScrollView = rootView.findViewById(R.id.nestedScrollView);
      if (nestedScrollView == null) {
        missingId = "nestedScrollView";
        break missingId;
      }
      RadioButton rbHorizontal = rootView.findViewById(R.id.rb_horizontal);
      if (rbHorizontal == null) {
        missingId = "rbHorizontal";
        break missingId;
      }
      RadioButton rbVertical = rootView.findViewById(R.id.rb_vertical);
      if (rbVertical == null) {
        missingId = "rbVertical";
        break missingId;
      }
      RadioGroup rgOrientation = rootView.findViewById(R.id.rg_orientation);
      if (rgOrientation == null) {
        missingId = "rgOrientation";
        break missingId;
      }
      AppCompatTextView textAttributesHeading = rootView.findViewById(R.id.text_attributes_heading);
      if (textAttributesHeading == null) {
        missingId = "textAttributesHeading";
        break missingId;
      }
      AppCompatTextView textOrientationHeading = rootView.findViewById(R.id.text_orientation_heading);
      if (textOrientationHeading == null) {
        missingId = "textOrientationHeading";
        break missingId;
      }
      ConstraintLayout viewBottomSheetAttributes = rootView.findViewById(R.id.view_bottom_sheet_attributes);
      if (viewBottomSheetAttributes == null) {
        missingId = "viewBottomSheetAttributes";
        break missingId;
      }
      return new BottomSheetOptionsBinding((ConstraintLayout) rootView, buttonApply, imageToggle,
          layoutLineBinding, layoutMarkerBinding, nestedScrollView, rbHorizontal, rbVertical,
          rgOrientation, textAttributesHeading, textOrientationHeading, viewBottomSheetAttributes);
    }
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
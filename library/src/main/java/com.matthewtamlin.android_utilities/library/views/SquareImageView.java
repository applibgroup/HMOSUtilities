/*
 * Copyright 2016 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.android_utilities.library.views;




import ohos.agp.components.AttrSet;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.global.resource.solidxml.TypedAttribute;

/**
 * An ImageView where one dimension is constrained so that both width and height are equal.
 *
 * @deprecated consider migrating to a ConstraintLayout based UI instead of using this class
 */
public class SquareImageView extends Image {
	private static final Dimension DEFAULT_CONSTRAINED_DIMENSION = Dimension.HEIGHT;

	private Dimension constrainedDimension;
	private Dimension SquareImageView_derivedDimension;

	/**
	 * Constructs a new SquareImageViewByWidth.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 */
	public SquareImageView(final Context context) {
		super(context);
		init(null, 0, 0);
	}

	/**
	 * Constructs a new SquareImageViewByWidth.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public SquareImageView(final Context context, final AttrSet attrs) {
		super(context, attrs);
		init(attrs, 0, 0);
	}

	/**
	 * Constructs a new SquareImageViewByWidth.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */


	/**
	 * Constructs a new SquareImageViewByWidth.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource which supplies default attributes, only used if {@code defStyleAttr}	is 0, pass
	 * 		0 to ignore
	 */


	/**
	 * Initialises the view. This method should only be called from a constructor.
	 *
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource which supplies default attributes, only used if {@code defStyleAttr}	is 0, pass
	 * 		0 to ignore
	 */
	private void init(final AttrSet attrs, final int defStyleAttr, final int defStyleRes) {
		final TypedAttribute attributes =null;
		int constrainedDimensionOrdinal=0;
		try {
			  constrainedDimensionOrdinal = attributes.getIntegerValue();
		}catch (Exception e){
			e.printStackTrace();
		}
		constrainedDimension = Dimension.values()[constrainedDimensionOrdinal];

	}

	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		super.estimateSize(widthMeasureSpec, heightMeasureSpec);

		final int dimensions = constrainedDimension == Dimension.WIDTH ?
				getHeight() :
				getWidth();

		setComponentMinSize(dimensions, dimensions);
	}

	private enum Dimension {
		WIDTH,
		HEIGHT
	}
}
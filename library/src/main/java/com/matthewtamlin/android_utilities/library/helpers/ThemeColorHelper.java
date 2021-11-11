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

package com.matthewtamlin.android_utilities.library.helpers;// not found

import android.content.Context;//done
import ohos.global.resource.solidxml.Theme;//done
import ohos.global.resource.solidxml.TypedAttribute;//done

import com.matthewtamlin.android_utilities.library.R;//not found
import com.matthewtamlin.java_utilities.testing.Tested;//not found

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;//not found

/**
 * Gets the core colors from the current theme.
 */
public class ThemeColorHelper {
	/**
	 * Gets the primary color from the current theme of the supplied Context.
	 *
	 * @param context
	 * 		the Context to get the color from, not null
	 * @param defaultColor
	 * 		the color to return if no primary color is found, as an ARGB hex code
	 *
	 * @return the primary color as an ARGB hex code, or the default color if none is found
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code context} is null
	 */
	public static int getPrimaryColor(final Context context, final int defaultColor) {
		checkNotNull(context, "context cannot be null.");

		return getColor(context, defaultColor, R.attr.colorPrimary);
	}

	/**
	 * Gets the primary dark color from the current theme of the supplied Context.
	 *
	 * @param context
	 * 		the Context to get the color from, not null
	 * @param defaultColor
	 * 		the color to return if no primary dark color is found, as an ARGB hex code
	 *
	 * @return the primary dark color as an ARGB hex code, or the default color if none is found
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code context} is null
	 */
	public static int getPrimaryDarkColor(final Context context, final int defaultColor) {
		checkNotNull(context, "context cannot be null.");

		return getColor(context, defaultColor, R.attr.colorPrimaryDark);
	}

	/**
	 * Gets the accent color from the current theme of the supplied Context.
	 *
	 * @param context
	 * 		the Context to get the color from, not null
	 * @param defaultColor
	 * 		the color to return if no accent color is found, as an ARGB hex code
	 *
	 * @return the accent color as an ARGB hex code, or the default color if none is found
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code context} is null
	 */
	public static int getAccentColor(final Context context, final int defaultColor) {
		checkNotNull(context, "context cannot be null.");

		return getColor(context, defaultColor, R.attr.colorAccent);
	}

	/**
	 * Gets a color from the current theme of the supplied Context.
	 *
	 * @param context
	 * 		the Context to get the color from, not null
	 * @param defaultColor
	 * 		the color to return if no color is found, as an ARGB hex code
	 * @param colorAttr
	 * 		an attribute in the current theme which identifies the color to return
	 *
	 * @return the color as an ARGB hex code, or the default color if none is found
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code context} is null
	 */
	private static int getColor(final Context context, final int defaultColor, final int
			colorAttr) {
		checkNotNull(context, "context cannot be null.");

		final TypedAttribute v = new TypedAttribute();
		final Theme a = context.obtainStyledAttributes(v.data, new int[]{colorAttr});
		final int color = a.getColor(0, defaultColor);

		a.recycle();

		return color;
	}
}
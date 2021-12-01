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

package com.matthewtamlin.android_utilities.library.helpers;

import ohos.global.resource.ResourceManager;
import ohos.media.image.Image;
import ohos.media.image.ImagePacker;
import ohos.media.image.ImageSource;
import ohos.media.image.ImageSource.DecodingOptions;
import ohos.media.image.PixelMap;

import java.io.File;

import static com.matthewtamlin.java_utilities.checkers.IntChecker.checkGreaterThanOrEqualTo;
import static com.matthewtamlin.java_utilities.checkers.IntChecker.checkLessThan;
import static com.matthewtamlin.java_utilities.checkers.IntChecker.checkLessThanOrEqualTo;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Decodes bitmaps efficiently.
 */
public class BitmapEfficiencyHelper  {
	/**
	 * Calculates the sampling rate which can be used to subsample an image to the desired
	 * dimensions. The sampling rate will satisfy all of the following criteria:
	 * <ul> <li>The sampling rate is a power of two.</li> <li>The height of the subsampled image
	 * will be at least the desired height. </li> <li>The width of the subsampled image will
	 * be at least the desired width.</li> </ul>.
	 * <p>
	 * If no sub-sampling is possible without violating one or more conditions, a sampling rate of
	 * 1 is returned.
	 *
	 * @param rawWidth
	 * 		the inherent width of the image before scaling, measured in pixels, not less than zero
	 * @param rawHeight
	 * 		the inherent height of the image before scaling, measured in pixels, not less than zero
	 * @param desWidth
	 * 		the desired width of the image after scaling, measured in pixels, not less than zero
	 * @param desHeight
	 * 		the desired height of the image after scaling, measured in pixels, not less than zero
	 *
	 * @return the sampling rate
	 *
	 * @throws IllegalArgumentException
	 * 		if any dimension is less than zero
	 */
	public static int calculateSamplingRate(
			final int rawWidth,
			final int rawHeight,
			final int desWidth,
			final int desHeight) {

		if (rawWidth < 0 || rawHeight < 0 || desWidth < 0 || desHeight < 0) {
			throw new IllegalArgumentException("All dimensions must be greater than zero.");
		}

		// Based on the power-of-two requirement
		final boolean scalingIsPossible = (rawWidth / 2 >= desWidth) &&
				(rawHeight / 2 >= desHeight);

		// Recursively double the sampling rate
		if (scalingIsPossible) {
			return 2 * calculateSamplingRate(rawWidth / 2, rawHeight / 2, desWidth, desHeight);
		} else {
			return 1;
		}
	}

	/**
	 * Decodes an image from a resource. The memory consumed by the decoded image is reduced by
	 * matching the image dimensions to the desired dimensions as best as possible. The dimensions
	 * of the returned image always exceed or match the supplied dimensions.
	 *
	 * @param res
	 * 		provides access to the resource to decode, not null
	 * @param resId
	 * 		the ID of the resource to decode
	 * @param desWidth
	 * 		the desired width of the decoded image, measured in pixels, not less than zero
	 * @param desHeight
	 * 		the desired height of the decoded image, measured in pixels, not less than zero
	 *
	 * @return the decoded image, or null if the image could not be decoded
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code context} is null
	 * @throws IllegalArgumentException
	 * 		if any dimension is less than zero
	 */
	public static PixelMap decodeResource(
			final ResourceManager res,
			final int resId,
			final int desWidth,
			final int desHeight) {

		checkNotNull(res, "res cannot be null.");
		checkGreaterThanOrEqualTo(desWidth, 0, "desWidth must be at least zero.");
		checkGreaterThanOrEqualTo(desHeight, 0, "desHeight must be at least zero.");

		// Decode only the boundaries of the image to get its dimensions
		final ImageSource.DecodingOptions options = new ImageSource.DecodingOptions();
		final ImageSource.SourceOptions options1=new ImageSource.SourceOptions();
		final ImageSource imageSource= ImageSource.createIncrementalSource(options1);
			options.allowPartialImage = true;
		//ImageSource.create(resId, options);//Different parameters


		// Decode the full image using sub-sampling
		final int rawWidth = options.desiredRegion.width;
		final int rawHeight = options.desiredRegion.height;
		options.sampleSize = calculateSamplingRate(rawWidth, rawHeight, desWidth, desHeight);
		options.allowPartialImage = false; // Decode the full image
		options.editable = false;
		PixelMap pixelMap = imageSource.createPixelmap(resId,options);
		return pixelMap;// ImageSource.createPixelMap(resId, options);//createPixelMap is not resolving
	}

	/**
	 * Decodes an image from an array of compressed image data. The memory consumed by the decoded
	 * image is reduced by matching the image dimensions to the desired dimensions as best as
	 * possible. The dimensions of the returned image always exceed or match the supplied
	 * dimensions.
	 *
	 * @param data
	 * 		a byte array of compressed image data, not null
	 * @param offset
	 * 		the offset into {@code data} to begin parsing at, counting from zero, not less than zero
	 * @param length
	 * 		the number of bytes at parse, not less than zero, less than {@code data.length - offset}
	 * @param desWidth
	 * 		the desired width of the decoded image, measured in pixels, not less than zero
	 * @param desHeight
	 * 		the desired height of the decoded image, measured in pixels, not less than zero
	 *
	 * @return the decoded image, or null if the image could not be decoded
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code data} is null
	 * @throws IllegalArgumentException
	 * 		if {@code offset} is not within the size limits of the data array
	 * @throws IllegalArgumentException
	 * 		if {@code length} is less than zero or greater than {@code data.length - offset}
	 * @throws IllegalArgumentException
	 * 		if {@code desWidth} is less than zero
	 * @throws IllegalArgumentException
	 * 		if {@code desHeight} is less than zero
	 */
	public static PixelMap decodeByteArray(final byte[] data, final int offset, final int length,
			final int desWidth, final int desHeight) {
		checkNotNull(data, "data cannot be null.");
		checkGreaterThanOrEqualTo(offset, 0, "offset must be at least zero.");
		checkLessThan(offset, data.length, "offset must be less than " + data.length);
		checkGreaterThanOrEqualTo(length, 0, "length must be at least zero.");
		checkLessThanOrEqualTo(length, data.length - offset, "length must be at most " +
				(data.length - offset));
		checkGreaterThanOrEqualTo(desWidth, 0, "desWidth must be at least zero.");
		checkGreaterThanOrEqualTo(desHeight, 0, "desHeight must be at least zero.");

		// Decode only the boundaries of the image to get its dimensions
		final ImageSource.DecodingOptions options = new ImageSource.DecodingOptions();
		final ImageSource.SourceOptions options1=new ImageSource.SourceOptions();
		final ImageSource imageSource= ImageSource.create(data, offset, length, options1);

		options.allowPartialImage = true;
		//ImageSource.create(data, offset, length, options);//different parameter.

		// Decode the full image using sub-sampling
		final int rawWidth = options.desiredRegion.width;
		final int rawHeight = options.desiredRegion.height;
		options.sampleSize = calculateSamplingRate(rawWidth, rawHeight, desWidth, desHeight);
		options.allowPartialImage = false; // Decode the full image
		options.editable = false;
		PixelMap pixelMap = imageSource.createPixelmap(options);
		return pixelMap;
	}

	/**
	 * Decodes an image from an array of compressed image data. The memory consumed by the decoded
	 * image is reduced by matching the image dimensions to the desired dimensions as best as
	 * possible. The dimensions of the returned image always exceed or match the supplied
	 * dimensions.
	 *
	 * @param data
	 * 		a byte array of compressed image data, not null
	 * @param desWidth
	 * 		the desired width of the decoded image, measured in pixels, not less than zero
	 * @param desHeight
	 * 		the desired height of the decoded image, measured in pixels, not less than zero
	 *
	 * @return the decoded image, or null if the image could not be decoded
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code data} is null
	 * @throws IllegalArgumentException
	 * 		if {@code desWidth} is less than zero
	 * @throws IllegalArgumentException
	 * 		if {@code desHeight} is less than zero
	 */
	public static PixelMap decodeByteArray(
			final byte[] data,
			final int desWidth,
			final int desHeight) {

		return decodeByteArray(data, 0, data.length, desWidth, desHeight);
	}

	/**
	 * Decodes an image from a file. The memory consumed by the decoded image is reduced by
	 * matching the image dimensions to the desired dimensions as best as possible. The
	 * dimensions of the returned image always exceed or match the supplied dimensions.
	 *
	 * @param file
	 * 		a file containing compressed image data, not null
	 * @param desWidth
	 * 		the desired width of the returned image, measured in pixels, not less than zero
	 * @param desHeight
	 * 		the desired height of the returned image, measured in pixels, not less than zero
	 *
	 * @return the decoded image, or null if the image could not be decoded
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code file} is null
	 * @throws IllegalArgumentException
	 * 		if {@code desWidth} is less than zero
	 * @throws IllegalArgumentException
	 * 		if {@code desHeight} is less than zero
	 */
	public static PixelMap decodeFile(final File file, final int desWidth, final int desHeight) {
		checkNotNull(file, "file cannot be null.");
		checkGreaterThanOrEqualTo(desWidth, 0, "desWidth must be at least zero.");
		checkGreaterThanOrEqualTo(desHeight, 0, "desHeight must be at least zero.");

		// Decode only the boundaries of the image to get its dimensions
		final ImageSource.DecodingOptions options = new ImageSource.DecodingOptions();
		final ImageSource.SourceOptions options1=new ImageSource.SourceOptions();
		final ImageSource imageSource= ImageSource.create(file.getAbsolutePath(),options1);
		options.allowPartialImage = true;
		//ImageSource.create(file.getAbsolutePath(), options);

		// Decode the full image using sub-sampling
		final int rawWidth = options.desiredRegion.width;
		final int rawHeight = options.desiredRegion.height;
		options.sampleSize = calculateSamplingRate(rawWidth, rawHeight, desWidth, desHeight);
		options.allowPartialImage = false;
		options.editable = false;
		PixelMap pixelMap = imageSource.createPixelmap(options);
		return pixelMap;
	}
}
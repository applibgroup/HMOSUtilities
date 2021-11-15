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

package com.matthewtamlin.android_utilities.library.utilities;

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Provides access to the UI thread using a Looper.
 *
 * @deprecated consider migrating to an RxJava based architecture instead of using this class
 */
@Deprecated
public class LooperUiThreadUtil implements UiThreadUtil {
	/**
	 * The looper which provides UI thread access.
	 */
	private final EventRunner looper;

	/**
	 * Constructs a new LooperUiThreadUtil which uses the supplied looper to access the UI thread.
	 *
	 * @param looper
	 * 		the looper to use, not null
	 *
	 * @return the new LooperUiThreadUtil, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code looper} is null
	 */
	public static LooperUiThreadUtil createUsingLooper(final EventRunner looper) {
		return new LooperUiThreadUtil(looper);
	}

	/**
	 * Constructs a new LooperUiThreadUtil which uses the main looper to access the UI thread.
	 *
	 * @return the new LooperUiThreadUtil, not null
	 */
	public static LooperUiThreadUtil createUsingMainLooper() {
		return new LooperUiThreadUtil(EventRunner.getMainEventRunner());
	}

	/**
	 * Constructs a new LooperUiThreadUtil.
	 *
	 * @param looper
	 * 		the looper to use, not null
	 */
	private LooperUiThreadUtil(final EventRunner looper) {
		this.looper = checkNotNull(looper, "looper cannot be null");
	}

	@Override
	public void runOnUiThread(final Runnable runnable) {
		if (runnable != null) {
			final EventHandler handler = new EventHandler(looper);
			handler.postTask(runnable);
		}
	}

	@Override
	public void runOnUiThreadWithDelay(final Runnable runnable, final long delayMilliseconds) {
		if (runnable != null) {
			final EventHandler handler = new EventHandler(looper);
			handler.postTask(runnable, delayMilliseconds);
		}
	}
}
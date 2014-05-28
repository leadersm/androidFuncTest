/*   Copyright 2004 BEA Systems, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.bea.xml.stream;

import java.util.Enumeration;


/**
 * Simple facotry to speed up creation process.
 * 
 * @author <a href="http://www.extreme.indiana.edu/~aslom/">Aleksander
 *         Slominski</a>
 */

public class MXParserFactory extends XmlPullParserFactory {
	protected static boolean stringCachedParserAvailable = true;

	@Override
	public XmlPullParser newPullParser() throws XmlPullParserException {
		XmlPullParser pp = null;
		if (stringCachedParserAvailable) {
			try {
				pp = (XmlPullParser) new MXParserCachingStrings();
			} catch (final Exception ex) {
				stringCachedParserAvailable = false;
			}
		}
		if (pp == null) {
			pp = (XmlPullParser) new MXParser();
		}
		for (final Enumeration<String> e = features.keys(); e.hasMoreElements();) {
			final String key = e.nextElement();
			final Boolean value = features.get(key);
			if (value != null && value.booleanValue()) {
				pp.setFeature(key, true);
			}
		}
		return pp;

	}

	@Override
	public XmlSerializer newSerializer() throws XmlPullParserException {
		return null;
	}
}



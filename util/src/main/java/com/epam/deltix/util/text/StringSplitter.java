/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.text;

import java.util.*;

public class StringSplitter {

	public static char[] defaultSeparators = new char[]
	                                       {
	        ',', ' ', ';', '\r', '\n', '\t'
	                                       };

	/**
	 * "a b 'c d' ''" -&gt; {"a", "b", "c d", ""}
	 * predefined separators ',', ' ', ';', '\r', '\n'
	 */
	public static String[] smartSplitSymbols ( String s ) {
		return smartSplitSymbols ( s,
		                           true );
	}

	public static List<String> smartSplitSymbols2 ( String s ) {
		String[] symbolArray = smartSplitSymbols ( s,
		                                           true );
		List<String> symbolList = new ArrayList<String> ( );
		Collections.addAll ( symbolList,
		                     symbolArray );
		return symbolList;
	}

	public static String[] smartSplitSymbols ( String s,
	                                           boolean quote2slash ) {
		return smartSplit ( s,
		                    quote2slash,
		                    defaultSeparators );
	}

	
	/**
	 * "a b 'c d' ''" -&gt; {"a", "b", "c d", ""}
	 */
	public static String[] smartSplit ( String s,
	                                    char[] separator ) {
		return smartSplit ( s,
		                    true,
		                    separator );
	}

	public static String[] smartSplit ( String s,
	                                    boolean quote2slash,
	                                    char[] separator ) {
		if (s == null)
			return new String[]
			{};

		if (separator == null || separator.length == 0)
			separator = defaultSeparators;

		Map<Character, Boolean> separatorTable = new HashMap<Character, Boolean> ( );
		for (int i = 0; i < separator.length; i++)
			separatorTable.put ( separator[i],
			                     true );

		List<String> result = new ArrayList<String> ( );

		for (int pos = 0; pos < s.length ( ); pos++) {
			if (Character.isWhitespace ( s.charAt ( pos ) ))
				continue;

			char endChar = separator[0];
			if (s.charAt ( pos ) == '\'' || s.charAt ( pos ) == '\"') {
				endChar = s.charAt ( pos );
				pos++;
			}

			int tickerLength = 0;

			boolean hasClosingQuote = false;
			for (; pos + tickerLength < s.length ( ); tickerLength++) {
				int curPos = pos + tickerLength;
				if (s.charAt ( curPos ) == '\r' || s.charAt ( curPos ) == '\n')
					break;

				if (endChar != separator[0]) {
					if (s.charAt ( curPos ) == endChar
					        && (curPos + 1 >= s.length ( ) || separatorTable.containsKey ( s.charAt ( curPos + 1 ) ))) {
						hasClosingQuote = true;
						break;
					}
				} else if (separatorTable.containsKey ( s.charAt ( curPos ) ))
					break;
			}

			if (endChar != separator[0] && !hasClosingQuote) {
				pos--;
				tickerLength++;
			}

			if (tickerLength > 0) {
				if (quote2slash)
					result.add ( s.substring ( pos,
					                           pos + tickerLength ).replace ( '\'',
					                                                          '\\' ) );
				else
					result.add ( s.substring ( pos,
					                           pos + tickerLength ) );
			}
			pos += tickerLength;
		}

		return result.toArray ( new String[result.size ( )] );
	}

	public static String quote2Slash ( String s ) {
		return s.replaceAll ( "'",
		                      "\\" );
	}

	/**
	 * Concatenates a specified list of symbol names from RecordBase to string in form: "A1 A2 'A 3' A4 ..."
	 */
	public static String join ( List<String> symbols ) {
		StringBuilder buf = new StringBuilder ( );
		for (int i = 0, iCount = symbols.size ( ); i < iCount; i++) {
			String symbol = symbols.get ( i );
			if (symbol.indexOf ( ' ' ) > 0) {
				buf.append ( formatSymbol ( symbol ) );
			} else {
				buf.append ( symbol );
			}

			if (i < (iCount - 1)) {
				buf.append ( ' ' );
			}
		}
		return buf.toString ( );
	}

	public static String formatSymbol ( String symbol ) {
		if (symbol.indexOf ( ' ' ) > 0) {
			return String.format ( "\'%s\'",
			                       symbol );
		} else {
			return symbol;
		}
	}

	/**
	 * Concatenates a specified list of symbol names to string in form: "'A1' 'A2' 'A 3' 'A4' ...".
	 * @param symbols - Symbols collection.
	 * @param separator - Symbols separator (for example ", "). Can be null. In this case symbols 
	 * will be separated using space (" ").
	 * @return
	 */
	public static String joinFormatted ( String[] symbols,
	                                     String separator ) {
		StringBuilder buf = new StringBuilder ( );
		int length = symbols.length;
		for (int i = 0; i < length; i++) {
			String symbol = symbols[i];
			buf.append ( formatSymbol ( symbol ) );
			if (i < (length - 1)) {
				if (separator == null)
					buf.append ( ' ' );
				else
					buf.append ( separator );
			}
		}
		return buf.toString ( );
	}
}
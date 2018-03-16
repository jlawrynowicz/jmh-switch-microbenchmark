package com.github.jlawrynowicz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class JmhMicrobenchmark {

	private static final int NUMBER_OF_METHOD_CALLS = 1_000_000;
	private static final int KEYWORD = 1;
	private static final int IDENTIFIER = 2;
	private static final int NULL = 3;
	private static final int TRUE = 4;
	private static final int FALSE = 5;
	private static final int ROWNUM = 6;
	private static HashMap<String, Integer> map;
	private static HashMap<String, Integer> mapWithFunctionsAsKeywords;

	static {
		mapWithFunctionsAsKeywords = new HashMap<>(43);

		mapWithFunctionsAsKeywords.put("FALSE", FALSE);
		mapWithFunctionsAsKeywords.put("TRUE", TRUE);
		mapWithFunctionsAsKeywords.put("NULL", NULL);
		mapWithFunctionsAsKeywords.put("ROWNUM", ROWNUM);
		mapWithFunctionsAsKeywords.put("ALL", KEYWORD);
		mapWithFunctionsAsKeywords.put("CHECK", KEYWORD);
		mapWithFunctionsAsKeywords.put("CONSTRAINT", KEYWORD);
		mapWithFunctionsAsKeywords.put("CROSS", KEYWORD);
		mapWithFunctionsAsKeywords.put("DISTINCT", KEYWORD);
		mapWithFunctionsAsKeywords.put("EXCEPT", KEYWORD);
		mapWithFunctionsAsKeywords.put("EXISTS", KEYWORD);
		mapWithFunctionsAsKeywords.put("FETCH", KEYWORD);
		mapWithFunctionsAsKeywords.put("FROM", KEYWORD);
		mapWithFunctionsAsKeywords.put("FOR", KEYWORD);
		mapWithFunctionsAsKeywords.put("FOREIGN", KEYWORD);
		mapWithFunctionsAsKeywords.put("FULL", KEYWORD);
		mapWithFunctionsAsKeywords.put("GROUP", KEYWORD);
		mapWithFunctionsAsKeywords.put("HAVING", KEYWORD);
		mapWithFunctionsAsKeywords.put("INNER", KEYWORD);
		mapWithFunctionsAsKeywords.put("INTERSECT", KEYWORD);
		mapWithFunctionsAsKeywords.put("IS", KEYWORD);
		mapWithFunctionsAsKeywords.put("JOIN", KEYWORD);
		mapWithFunctionsAsKeywords.put("LIMIT", KEYWORD);
		mapWithFunctionsAsKeywords.put("LIKE", KEYWORD);
		mapWithFunctionsAsKeywords.put("MINUS", KEYWORD);
		mapWithFunctionsAsKeywords.put("NOT", KEYWORD);
		mapWithFunctionsAsKeywords.put("NATURAL", KEYWORD);
		mapWithFunctionsAsKeywords.put("OFFSET", KEYWORD);
		mapWithFunctionsAsKeywords.put("ON", KEYWORD);
		mapWithFunctionsAsKeywords.put("ORDER", KEYWORD);
		mapWithFunctionsAsKeywords.put("PRIMARY", KEYWORD);
		mapWithFunctionsAsKeywords.put("SELECT", KEYWORD);
		mapWithFunctionsAsKeywords.put("TODAY", KEYWORD);
		mapWithFunctionsAsKeywords.put("UNIQUE", KEYWORD);
		mapWithFunctionsAsKeywords.put("UNION", KEYWORD);
		mapWithFunctionsAsKeywords.put("WITH", KEYWORD);
		mapWithFunctionsAsKeywords.put("WHERE", KEYWORD);
		mapWithFunctionsAsKeywords.put("CURRENT_DATE", KEYWORD);
		mapWithFunctionsAsKeywords.put("CURRENT_TIME", KEYWORD);
		mapWithFunctionsAsKeywords.put("CURRENT_TIMESTAMP", KEYWORD);
		mapWithFunctionsAsKeywords.put("SYSDATE", KEYWORD);
		mapWithFunctionsAsKeywords.put("SYSTIME", KEYWORD);
		mapWithFunctionsAsKeywords.put("SYSTIMESTAMP", KEYWORD);

		map = new HashMap<>(37);

		map.put("FALSE", FALSE);
		map.put("TRUE", TRUE);
		map.put("NULL", NULL);
		map.put("ROWNUM", ROWNUM);
		map.put("ALL", KEYWORD);
		map.put("CHECK", KEYWORD);
		map.put("CONSTRAINT", KEYWORD);
		map.put("CROSS", KEYWORD);
		map.put("DISTINCT", KEYWORD);
		map.put("EXCEPT", KEYWORD);
		map.put("EXISTS", KEYWORD);
		map.put("FETCH", KEYWORD);
		map.put("FROM", KEYWORD);
		map.put("FOR", KEYWORD);
		map.put("FOREIGN", KEYWORD);
		map.put("FULL", KEYWORD);
		map.put("GROUP", KEYWORD);
		map.put("HAVING", KEYWORD);
		map.put("INNER", KEYWORD);
		map.put("INTERSECT", KEYWORD);
		map.put("IS", KEYWORD);
		map.put("JOIN", KEYWORD);
		map.put("LIMIT", KEYWORD);
		map.put("LIKE", KEYWORD);
		map.put("MINUS", KEYWORD);
		map.put("NOT", KEYWORD);
		map.put("NATURAL", KEYWORD);
		map.put("OFFSET", KEYWORD);
		map.put("ON", KEYWORD);
		map.put("ORDER", KEYWORD);
		map.put("PRIMARY", KEYWORD);
		map.put("SELECT", KEYWORD);
		map.put("TODAY", KEYWORD);
		map.put("UNIQUE", KEYWORD);
		map.put("UNION", KEYWORD);
		map.put("WITH", KEYWORD);
		map.put("WHERE", KEYWORD);
	}

	private String[] POSSIBLE_TEST_INPUTS = { "FALSE", "TRUE", "NULL", "ROWNUM", "ALL", "CHECK", "CONSTRAINT", "CROSS",
			"DISTINCT", "EXCEPT", "EXISTS", "FETCH", "FROM", "FOR", "FOREIGN", "FULL", "GROUP", "HAVING", "INNER",
			"INTERSECT", "IS", "JOIN", "LIMIT", "LIKE", "MINUS", "NOT", "NATURAL", "OFFSET", "ON", "ORDER", "PRIMARY",
			"SELECT", "TODAY", "UNIQUE", "UNION", "WITH", "WHERE", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP",
			"SYSDATE", "SYSTIME", "SYSTIMESTAMP",
			// some identifiers mixed in
			"IDENTIFIER1", "A_IDENTIFIER", "B_IDENTIFIER", "C_IDENTIFIER", "D_IDENTIFIER", "E_IDENTIFIER",
			"F_IDENTIFIER", "G_IDENTIFIER", "H_IDENTIFIER", "I_IDENTIFIER", "J_IDENTIFIER" };
	private boolean FUNCTIONS_AS_KEYWORDS_VALUE = true;
	private List<String> randomTestInputs;

	public static int getSaveTokenType_IF_ELSE(String s, boolean functionsAsKeywords) {
		if (s.charAt(0) == 'A') {
			return getKeywordOrIdentifier(s, "ALL", KEYWORD);
		} else if (s.charAt(0) == 'C') {
			if ("CHECK".equals(s)) {
				return KEYWORD;
			} else if ("CONSTRAINT".equals(s)) {
				return KEYWORD;
			} else if ("CROSS".equals(s)) {
				return KEYWORD;
			}
			if (functionsAsKeywords) {
				if ("CURRENT_DATE".equals(s) || "CURRENT_TIME".equals(s) || "CURRENT_TIMESTAMP".equals(s)) {
					return KEYWORD;
				}
			}
			return IDENTIFIER;
		} else if (s.charAt(0) == 'D') {
			return getKeywordOrIdentifier(s, "DISTINCT", KEYWORD);
		} else if (s.charAt(0) == 'E') {
			if ("EXCEPT".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "EXISTS", KEYWORD);
		} else if (s.charAt(0) == 'F') {
			if ("FETCH".equals(s)) {
				return KEYWORD;
			} else if ("FROM".equals(s)) {
				return KEYWORD;
			} else if ("FOR".equals(s)) {
				return KEYWORD;
			} else if ("FOREIGN".equals(s)) {
				return KEYWORD;
			} else if ("FULL".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "FALSE", FALSE);
		} else if (s.charAt(0) == 'G') {
			return getKeywordOrIdentifier(s, "GROUP", KEYWORD);
		} else if (s.charAt(0) == 'H') {
			return getKeywordOrIdentifier(s, "HAVING", KEYWORD);
		} else if (s.charAt(0) == 'I') {
			if ("INNER".equals(s)) {
				return KEYWORD;
			} else if ("INTERSECT".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "IS", KEYWORD);
		} else if (s.charAt(0) == 'J') {
			return getKeywordOrIdentifier(s, "JOIN", KEYWORD);
		} else if (s.charAt(0) == 'L') {
			if ("LIMIT".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "LIKE", KEYWORD);
		} else if (s.charAt(0) == 'M') {
			return getKeywordOrIdentifier(s, "MINUS", KEYWORD);
		} else if (s.charAt(0) == 'N') {
			if ("NOT".equals(s)) {
				return KEYWORD;
			} else if ("NATURAL".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "NULL", NULL);
		} else if (s.charAt(0) == 'O') {
			if ("OFFSET".equals(s)) {
				return KEYWORD;
			} else if ("ON".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "ORDER", KEYWORD);
		} else if (s.charAt(0) == 'P') {
			return getKeywordOrIdentifier(s, "PRIMARY", KEYWORD);
		} else if (s.charAt(0) == 'R') {
			return getKeywordOrIdentifier(s, "ROWNUM", ROWNUM);
		} else if (s.charAt(0) == 'S') {
			if ("SELECT".equals(s)) {
				return KEYWORD;
			}
			if (functionsAsKeywords) {
				if ("SYSDATE".equals(s) || "SYSTIME".equals(s) || "SYSTIMESTAMP".equals(s)) {
					return KEYWORD;
				}
			}
			return IDENTIFIER;
		} else if (s.charAt(0) == 'T') {
			if ("TRUE".equals(s)) {
				return TRUE;
			}
			if (functionsAsKeywords) {
				if ("TODAY".equals(s)) {
					return KEYWORD;
				}
			}
			return IDENTIFIER;
		} else if (s.charAt(0) == 'U') {
			if ("UNIQUE".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "UNION", KEYWORD);
		} else if (s.charAt(0) == 'W') {
			if ("WITH".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "WHERE", KEYWORD);
		} else {
			return IDENTIFIER;
		}
	}

	public static int getSaveTokenType_SWITCH_WITH_IF_ELSE(String s, boolean functionsAsKeywords) {
		switch (s.charAt(0)) {
		case 'A':
			return getKeywordOrIdentifier(s, "ALL", KEYWORD);
		case 'C':
			if ("CHECK".equals(s)) {
				return KEYWORD;
			} else if ("CONSTRAINT".equals(s)) {
				return KEYWORD;
			} else if ("CROSS".equals(s)) {
				return KEYWORD;
			}
			if (functionsAsKeywords) {
				if ("CURRENT_DATE".equals(s) || "CURRENT_TIME".equals(s) || "CURRENT_TIMESTAMP".equals(s)) {
					return KEYWORD;
				}
			}
			return IDENTIFIER;
		case 'D':
			return getKeywordOrIdentifier(s, "DISTINCT", KEYWORD);
		case 'E':
			if ("EXCEPT".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "EXISTS", KEYWORD);
		case 'F':
			if ("FETCH".equals(s)) {
				return KEYWORD;
			} else if ("FROM".equals(s)) {
				return KEYWORD;
			} else if ("FOR".equals(s)) {
				return KEYWORD;
			} else if ("FOREIGN".equals(s)) {
				return KEYWORD;
			} else if ("FULL".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "FALSE", FALSE);
		case 'G':
			return getKeywordOrIdentifier(s, "GROUP", KEYWORD);
		case 'H':
			return getKeywordOrIdentifier(s, "HAVING", KEYWORD);
		case 'I':
			if ("INNER".equals(s)) {
				return KEYWORD;
			} else if ("INTERSECT".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "IS", KEYWORD);
		case 'J':
			return getKeywordOrIdentifier(s, "JOIN", KEYWORD);
		case 'L':
			if ("LIMIT".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "LIKE", KEYWORD);
		case 'M':
			return getKeywordOrIdentifier(s, "MINUS", KEYWORD);
		case 'N':
			if ("NOT".equals(s)) {
				return KEYWORD;
			} else if ("NATURAL".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "NULL", NULL);
		case 'O':
			if ("OFFSET".equals(s)) {
				return KEYWORD;
			} else if ("ON".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "ORDER", KEYWORD);
		case 'P':
			return getKeywordOrIdentifier(s, "PRIMARY", KEYWORD);
		case 'R':
			return getKeywordOrIdentifier(s, "ROWNUM", ROWNUM);
		case 'S':
			if ("SELECT".equals(s)) {
				return KEYWORD;
			}
			if (functionsAsKeywords) {
				if ("SYSDATE".equals(s) || "SYSTIME".equals(s) || "SYSTIMESTAMP".equals(s)) {
					return KEYWORD;
				}
			}
			return IDENTIFIER;
		case 'T':
			if ("TRUE".equals(s)) {
				return TRUE;
			}
			if (functionsAsKeywords) {
				if ("TODAY".equals(s)) {
					return KEYWORD;
				}
			}
			return IDENTIFIER;
		case 'U':
			if ("UNIQUE".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "UNION", KEYWORD);
		case 'W':
			if ("WITH".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "WHERE", KEYWORD);
		default:
			return IDENTIFIER;
		}
	}

	public static int getSaveTokenType_SWITCH(String s, boolean functionsAsKeywords) {
		switch (s.charAt(0)) {
		case 'A':
			return getKeywordOrIdentifier(s, "ALL", KEYWORD);
		case 'C':
			switch (s) {
			case "CHECK":
				return KEYWORD;
			case "CONSTRAINT":
				return KEYWORD;
			case "CROSS":
				return KEYWORD;
			}
			if (functionsAsKeywords) {
				if ("CURRENT_DATE".equals(s) || "CURRENT_TIME".equals(s) || "CURRENT_TIMESTAMP".equals(s)) {
					return KEYWORD;
				}
			}
			return IDENTIFIER;
		case 'D':
			return getKeywordOrIdentifier(s, "DISTINCT", KEYWORD);
		case 'E':
			if ("EXCEPT".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "EXISTS", KEYWORD);
		case 'F':
			switch (s) {
			case "FETCH":
				return KEYWORD;
			case "FROM":
				return KEYWORD;
			case "FOR":
				return KEYWORD;
			case "FOREIGN":
				return KEYWORD;
			case "FULL":
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "FALSE", FALSE);
		case 'G':
			return getKeywordOrIdentifier(s, "GROUP", KEYWORD);
		case 'H':
			return getKeywordOrIdentifier(s, "HAVING", KEYWORD);
		case 'I':
			if ("INNER".equals(s)) {
				return KEYWORD;
			} else if ("INTERSECT".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "IS", KEYWORD);
		case 'J':
			return getKeywordOrIdentifier(s, "JOIN", KEYWORD);
		case 'L':
			if ("LIMIT".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "LIKE", KEYWORD);
		case 'M':
			return getKeywordOrIdentifier(s, "MINUS", KEYWORD);
		case 'N':
			if ("NOT".equals(s)) {
				return KEYWORD;
			} else if ("NATURAL".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "NULL", NULL);
		case 'O':
			if ("OFFSET".equals(s)) {
				return KEYWORD;
			} else if ("ON".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "ORDER", KEYWORD);
		case 'P':
			return getKeywordOrIdentifier(s, "PRIMARY", KEYWORD);
		case 'R':
			return getKeywordOrIdentifier(s, "ROWNUM", ROWNUM);
		case 'S':
			if ("SELECT".equals(s)) {
				return KEYWORD;
			}
			if (functionsAsKeywords) {
				if ("SYSDATE".equals(s) || "SYSTIME".equals(s) || "SYSTIMESTAMP".equals(s)) {
					return KEYWORD;
				}
			}
			return IDENTIFIER;
		case 'T':
			if ("TRUE".equals(s)) {
				return TRUE;
			}
			if (functionsAsKeywords) {
				if ("TODAY".equals(s)) {
					return KEYWORD;
				}
			}
			return IDENTIFIER;
		case 'U':
			if ("UNIQUE".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "UNION", KEYWORD);
		case 'W':
			if ("WITH".equals(s)) {
				return KEYWORD;
			}
			return getKeywordOrIdentifier(s, "WHERE", KEYWORD);
		default:
			return IDENTIFIER;
		}
	}

	public static int getSaveTokenType_SIMPLIFIED_SWITCH(String s, boolean functionsAsKeywords) {
		switch (s) {
		case "FALSE":
			return FALSE;
		case "TRUE":
			return TRUE;
		case "NULL":
			return NULL;
		case "ROWNUM":
			return ROWNUM;
		case "ALL":
		case "CHECK":
		case "CONSTRAINT":
		case "CROSS":
		case "DISTINCT":
		case "EXCEPT":
		case "EXISTS":
		case "FETCH":
		case "FROM":
		case "FOR":
		case "FOREIGN":
		case "FULL":
		case "GROUP":
		case "HAVING":
		case "INNER":
		case "INTERSECT":
		case "IS":
		case "JOIN":
		case "LIMIT":
		case "LIKE":
		case "MINUS":
		case "NOT":
		case "NATURAL":
		case "OFFSET":
		case "ON":
		case "ORDER":
		case "PRIMARY":
		case "SELECT":
		case "TODAY":
		case "UNIQUE":
		case "UNION":
		case "WITH":
		case "WHERE":
			return KEYWORD;
		case "CURRENT_DATE":
		case "CURRENT_TIME":
		case "CURRENT_TIMESTAMP":
		case "SYSDATE":
		case "SYSTIME":
		case "SYSTIMESTAMP":
			if (functionsAsKeywords) {
				return KEYWORD;
			}
		default:
			return IDENTIFIER;
		}
	}

	public static int getSaveTokenType_HASH_MAP(String s, boolean functionsAsKeywords) {

		Integer integer;
		if (functionsAsKeywords) {
			integer = mapWithFunctionsAsKeywords.get(s);
		} else {
			integer = map.get(s);
		}

		if (integer == null) {
			return IDENTIFIER;
		}
		return integer;
	}

	private static int getKeywordOrIdentifier(String s1, String s2, int keywordType) {
		if (s1.equals(s2)) {
			return keywordType;
		}
		return IDENTIFIER;
	}

	@Setup()
	public void setup() {
		System.out.println("RUNNING SETUP");
		Random random = new Random();
		randomTestInputs = new ArrayList<>(NUMBER_OF_METHOD_CALLS);
		for (int i = 0; i < NUMBER_OF_METHOD_CALLS; i++) {
			randomTestInputs.add(POSSIBLE_TEST_INPUTS[random.nextInt(POSSIBLE_TEST_INPUTS.length)]);
		}

	}

	@Benchmark
	public void switch_with_if_else(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_METHOD_CALLS; i++) {
			int result = getSaveTokenType_SWITCH_WITH_IF_ELSE(randomTestInputs.get(i), FUNCTIONS_AS_KEYWORDS_VALUE);
			blackhole.consume(result);
		}
	}

	@Benchmark
	public void switch_nested(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_METHOD_CALLS; i++) {
			int result = getSaveTokenType_SWITCH(randomTestInputs.get(i), FUNCTIONS_AS_KEYWORDS_VALUE);
			blackhole.consume(result);
		}
	}

	@Benchmark
	public void switch_simplified(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_METHOD_CALLS; i++) {
			int result = getSaveTokenType_SIMPLIFIED_SWITCH(randomTestInputs.get(i), FUNCTIONS_AS_KEYWORDS_VALUE);
			blackhole.consume(result);
		}
	}

	@Benchmark
	public void if_else(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_METHOD_CALLS; i++) {
			int result = getSaveTokenType_IF_ELSE(randomTestInputs.get(i), FUNCTIONS_AS_KEYWORDS_VALUE);
			blackhole.consume(result);
		}
	}

	@Benchmark
	public void hashMap(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_METHOD_CALLS; i++) {
			int result = getSaveTokenType_HASH_MAP(randomTestInputs.get(i), FUNCTIONS_AS_KEYWORDS_VALUE);
			blackhole.consume(result);
		}
	}
}

/*
 * Sample instrumentation for use with jarsurgeon.rb
 * Copyright (c) 2014 Kevin Cernekee <cernekee@gmail.com>
 *
 * This program is distributed under the MIT License.  See LICENSE in
 * the source distribution for details.
 */

package inst;

import java.lang.Object;
import java.lang.String;
import java.lang.System;

public class Inst {

	private static void printhex(String pfx, byte in[], int start, int len) {
		int bytes = 0;

		pfx = ";" + pfx;

		for (int i = 0; i < in.length; i++) {
			if (bytes == 0) {
				System.out.print(pfx);
			}
			if (i == start && len != 0) {
				System.out.print(">");
			} else if (i == (start + len) && len != 0) {
				System.out.print("<");
			} else {
				System.out.print(" ");
			}
			System.out.print(String.format("%02x", in[i]));

			bytes++;
			if (bytes == 16) {
				bytes = 0;
				System.out.println("");
			}
		}
		if (bytes != 0) {
			System.out.println("");
		}
	}

	// invokestatic inst/Inst printhex (Ljava/lang/String;[C)V
	public static void printhex(String pfx, char in[]) {
		byte tmp[] = new byte[in.length * 2];
		for (int i = 0; i < in.length; i++) {
			tmp[i*2] = (byte)((in[i] & 0xff00) >> 8);
			tmp[i*2+1] = (byte)((in[i] & 0xff) >> 0);
		}
		printhex(pfx, tmp, 0, 0);
	}

	// invokestatic inst/Inst printhex (Ljava/lang/String;[[B)V
	public static void printhex(String pfx, byte in[][]) {
		for (int i = 0; i < in.length; i++) {
			printhex(pfx + "[" + i + "]: ", in[i], 0, 0);
		}
	}

	// invokestatic inst/Inst printhex (Ljava/lang/String;[B)V
	public static void printhex(String pfx, byte in[]) {
		printhex(pfx, in, 0, 0);
	}

	// invokestatic inst/Inst printint (Ljava/lang/String;I)V
	public static void printint(String pfx, int in) {
		System.out.println(";" + pfx + in);
	}

	// invokestatic inst/Inst println (Ljava/lang/String;)V
	public static void println(String s) {
		System.out.println(";" + s);
	}

	// invokestatic inst/Inst arraycopy (Ljava/lang/Object;ILjava/lang/Object;II)V
	// (this is a drop-in replacement for java.lang.System.arraycopy)
	public static void arraycopy(Object arg0, int arg1, Object arg2, int arg3, int arg4) {
		if (arg0 instanceof byte[]) {
			new Exception().printStackTrace();
			System.out.println(";ARRAY COPY: " + arg4 +
				" bytes (offset " + arg1 + " -> " + arg3 + ")");
			printhex("  SRC:", (byte[])arg0, arg1, arg4);
			System.out.println("");
			printhex("  DST:", (byte[])arg2, arg3, arg4);
			System.out.println("");
		}
		System.arraycopy(arg0, arg1, arg2, arg3, arg4);
	}

};

package com.androd.bugreporter.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExecTools {
	public static String execSuCommand(String cmd) throws IOException {

		System.err.println("suִ�п�ʼ");
		Process process = Runtime.getRuntime().exec("su");
		DataOutputStream os = new DataOutputStream(
				process.getOutputStream());
		os.writeBytes(cmd + "\n");
		os.flush();
		os.writeBytes("exit\n");
		os.flush();

		BufferedReader reader_e = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));
		int read_e;
		char[] buffer_e = new char[4096];
		StringBuffer output_e = new StringBuffer();
		while ((read_e = reader_e.read(buffer_e)) > 0) {
			output_e.append(buffer_e, 0, read_e);
		}
		reader_e.close();

		System.out.println(output_e.toString());

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		int read;
		char[] buffer = new char[4096];
		StringBuffer output = new StringBuffer();
		while ((read = reader.read(buffer)) > 0) {
			output.append(buffer, 0, read);
		}
		reader.close();
		os.close();

		System.err.println("suִ�н���");
		return output.toString();
	}

	public static String execCommand(String command) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(command);

		System.err.println("ִ�п�ʼ");

		try {
			System.err.println("ִ�п�ʼ2");

			if (proc.waitFor() != 0) {

				System.err.println("exit value1 = " + proc.exitValue());
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(proc.getErrorStream()));
				int read;
				char[] buffer = new char[4096];
				StringBuffer output = new StringBuffer();
				while ((read = reader.read(buffer)) > 0) {
					output.append(buffer, 0, read);
				}
				reader.close();

				System.out.println(output.toString());
				return output.toString();

			} else {

				System.err.println("ִ�����");
			}

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(proc.getInputStream()));
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();

			System.out.println(output.toString());
			return output.toString();

		} catch (InterruptedException e) {

			System.err.println(e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		}

	}

	public static String execCommandArray(String[] command)
			throws IOException {
		String str = "";
		Runtime runtime = Runtime.getRuntime();

		// Process proc = runtime.exec(new
		// String[]{"sh","-c","getprop|grep ip"});
		Process proc = runtime.exec(command);

		System.err.println("execCommandArray��ʼ");
		try {
			if (proc.waitFor() != 0) {
				System.err.println("exit value = " + proc.exitValue());
				if (proc.exitValue() == 1) {

					System.err.println("execCommandArrayִ�з����޽�����߲�ѯΪ��");
					// return str;
				}

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(proc.getErrorStream()));
				int read;
				char[] buffer = new char[4096];
				StringBuffer output = new StringBuffer();
				while ((read = reader.read(buffer)) > 0) {
					output.append(buffer, 0, read);
				}
				reader.close();

				System.out.println(output.toString());
				return str;

			} else {
				System.err.println("execCommandArray����");
			}

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(proc.getInputStream()));
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();

			System.out.println(output.toString());
			// return output.toString();
			str = output.toString().trim();
			return str;

		} catch (InterruptedException e) {

			System.err.println(e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			// return sw.toString();
			return str;

		}

	}
}

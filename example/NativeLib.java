package example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class NativeLib {

	/* Statically load the library which contains all native functions used in here */
	static private boolean isNativeInited = false;
	static String name = "example_swig";

	public static String getPath() {
		final String prefix = "NATIVE";
		String os = System.getProperty("os.name");
		String arch = System.getProperty("os.arch");

		if (os.toLowerCase().startsWith("^win")) {
			os = "Windows";
		} else if (os.contains("OS X")) {
			os = "Darwin";
		}

		if (arch.matches("^i[3-6]86$")) {
			arch = "x86";
		} else if (arch.equalsIgnoreCase("amd64")) {
			arch = "x86_64";
		}

		os = os.replace(' ', '_');
		arch = arch.replace(' ', '_');

		return prefix + "/" + os + "/" + arch + "/";
	}

	public static void nativeInit() {
		if (isNativeInited) {
			return;
		}
		try {
			/* prefer the version on disk, if existing */
			System.loadLibrary(name);
		} catch (final UnsatisfiedLinkError e) {
			/* If not found, unpack the one bundled into the jar file and use it */
			loadLib(name);
		}
		isNativeInited = true;
	}

	private static void loadLib(String name) {
		final String Path = NativeLib.getPath();

		String filename = name;
		InputStream in = NativeLib.class.getClassLoader().getResourceAsStream(Path + filename);

		if (in == null) {
			filename = "lib" + name + ".so";
			in = NativeLib.class.getClassLoader().getResourceAsStream(Path + filename);
		}
		if (in == null) {
			filename = name + ".dll";
			in = NativeLib.class.getClassLoader().getResourceAsStream(Path + filename);
		}
		if (in == null) {
			filename = "lib" + name + ".dll";
			in = NativeLib.class.getClassLoader().getResourceAsStream(Path + filename);
		}
		if (in == null) {
			filename = "lib" + name + ".dylib";
			in = NativeLib.class.getClassLoader().getResourceAsStream(Path + filename);
		}
		if (in == null) {
			throw new RuntimeException(
					"Cannot find library " + name + " in path " + Path + ". Sorry, but this jar does not seem to be usable on your machine.");
		}
		try {
			// We must write the lib onto the disk before loading it -- stupid operating systems
			File fileOut = new File(filename);
			fileOut = File.createTempFile(name + "-", ".tmp");
			// don't leak the file on disk, but remove it on JVM shutdown
			Runtime.getRuntime().addShutdownHook(new Thread(new FileCleaner(fileOut.getAbsolutePath())));
			final OutputStream out = new FileOutputStream(fileOut);

			/* copy the library in position */
			final byte[] buffer = new byte[4096];
			int bytes_read;
			while ((bytes_read = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes_read);
			}

			/* close all file descriptors, and load that shit */
			in.close();
			out.close();
			System.load(fileOut.getAbsolutePath());

		} catch (final Exception e) {
			System.err.println("Cannot load the bindings to the " + name + " library: ");
			e.printStackTrace();
			System.err.println("This jar file does not seem to fit your system, sorry");
			System.exit(1);
		}
	}

	/* A hackish mechanism used to remove the file containing our library when the JVM shuts down */
	private static class FileCleaner implements Runnable {
		private final String target;

		public FileCleaner(String name) {
			target = name;
		}

		@Override
		public void run() {
			try {
				new File(target).delete();
			} catch (final Exception e) {
				System.out.println("Unable to clean temporary file " + target + " during shutdown.");
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(getPath());
	}
}

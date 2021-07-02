package de.retest.recheck.cli.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.NoSuchFileException;

import com.esotericsoftware.kryo.kryo5.KryoException;

import de.retest.recheck.RecheckProperties;
import de.retest.recheck.cli.TestReportFormatException;
import de.retest.recheck.persistence.IncompatibleReportVersionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorHandler {

	private ErrorHandler() {}

	public static void handle( final Exception e ) {
		if ( e instanceof TestReportFormatException ) {
			log.error( "The given file is not a test report. Please only pass files using the '{}' extension.",
					RecheckProperties.TEST_REPORT_FILE_EXTENSION );
			return;
		}
		if ( e instanceof NoSuchFileException ) {
			log.error( "The given file report '{}' does not exist. Please check the given file path.",
					((NoSuchFileException) e).getFile() );
			log.debug( "Stack trace:", e );
			return;
		}
		if ( e instanceof KryoException || e instanceof IncompatibleReportVersionException ) {
			log.error( "The report was created with another, incompatible recheck version.\n"
					+ "Please use the same recheck version to load a report with which it was generated." );
			log.debug( "Stack trace:", e );
			return;
		}
		if ( e instanceof UncheckedIOException ) {
			log.error( "{}", e.getMessage() );
			return;
		}
		if ( e instanceof IOException ) {
			log.error( "An error occurred while loading or saving the test report.", e );
			return;
		}
		throw new RuntimeException( e );
	}

}

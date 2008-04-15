//
// This file is part of InfoGrid(tm). You may not use this file except in
// compliance with the InfoGrid license. The InfoGrid license and important
// disclaimers are contained in the file LICENSE.InfoGrid.txt that you should
// have received with InfoGrid. If you have not received LICENSE.InfoGrid.txt
// or you do not consent to all aspects of the license and the disclaimers,
// no license is granted; do not use this file.
// 
// For more information about InfoGrid go to http://infogrid.org/
//
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.codegen;

import org.infogrid.model.primitives.CollectableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.L10Map;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.ModelPrimitivesStringRepresentation;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.SubjectArea;

import org.infogrid.util.logging.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

/**
 * Abstract superclass for both InterfaceGenerator and ImplementationGenerator. It
 * factors out common code.
 */
public abstract class AbstractGenerator
{
    private static final Log log = Log.getLogInstance(AbstractGenerator.class); // our own, private logger

    /**
      * Constructor.
      *
      * @param outputDir the directory into which the code shall be genreated
      */
    public AbstractGenerator(
            File outputDir )
    {
        theOutputDir = outputDir;
    }

    /**
      * Generate code for all MeshTypes in the specified SubjectAreas.
      *
      * @param sas the SubjectAreas to generate code from
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    public void generateForAll(
            SubjectArea [] sas )
        throws
            IOException
    {
        for( SubjectArea sa : sas ) {
            generateCodeForSubjectArea( sa );
            generateJavaDocForSubjectArea( sa );
            for( CollectableMeshType cmt : sa.getCollectableMeshTypes() ) {
                if( cmt instanceof EntityType ) {
                    generateCodeForEntityType( (EntityType) cmt );
                }
            }
        }
    }

    /**
      * Generate code for one EntityType.
      *
      * @param theEntityType the EntityType to generate code for
      * @return the fully-qualified file name where it was generated
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    protected abstract String generateCodeForEntityType(
            EntityType theEntityType )
        throws
            IOException;

    /**
     * Generate code for one SubjectArea.
     *
     * @param theSubjectArea the SubjectArea to generate code for
     * @return the fully-qualified file name where it was generated
     * @throws IOException thrown if an I/O error occurred during code generation
     */
    protected String generateCodeForSubjectArea(
            SubjectArea theSubjectArea )
        throws
            IOException
    {
        // no op
        return null;
    }

    /**
     * Generate the JavaDoc documentation for one SubjectArea.
     *
     * @param theSubjectArea the SubjectArea to generate documentation for
     * @return the fully-qualified file name where it was generated
     * @throws IOException thrown if an I/O error occurred during code generation
     */
    protected abstract String generateJavaDocForSubjectArea(
            SubjectArea theSubjectArea )
        throws
            IOException;

    /**
      * Obtain the name of the sub-package that contains the interfaces.
      * We don't have any, it's directly in the subject area package.
      *
      * @return the empty string
      */
    protected String getInterfaceSubPackageName()
    {
        return "";
    }

    /**
     * Generate the JavaDoc for a PropertyType.
     *
     * @param thePropertyType the PropertyType for which we generate documentation
     * @param w the PrintWriter to write to
     */
    protected void generatePropertyTypeJavaDoc(
            PropertyType thePropertyType,
            PrintWriter  w )
    {
        w.println( "      * <table>" );
        w.println( "      *  <tr><td>Identifier:</td><td><tt>"
                + thePropertyType.getIdentifier().toExternalForm()
                + "</tt></td></tr>" );
        w.println( "      *  <tr><td>Name:</td><td><tt>"
                + PropertyValue.toStringRepresentation( thePropertyType.getName(), ModelPrimitivesStringRepresentation.TEXT_HTML )
                + "</tt></td></tr>" );
        w.println( "      *  <tr><td>DataType:</td><td><tt>"
                + thePropertyType.getDataType().toStringRepresentation( ModelPrimitivesStringRepresentation.TEXT_HTML )
                + "</tt></td></tr>" );
        w.println( "      *  <tr><td>DefaultValue:</td><td><tt>"
                + PropertyValue.toStringRepresentation( thePropertyType.getDefaultValue(), ModelPrimitivesStringRepresentation.TEXT_HTML )
                + "</tt></td></tr>" );
        w.println( "      *  <tr><td>IsOptional:</td><td><tt>"
                + PropertyValue.toStringRepresentation( thePropertyType.getIsOptional(), ModelPrimitivesStringRepresentation.TEXT_HTML )
                + "</tt></td></tr>" );
        w.println( "      *  <tr><td>IsReadOnly:</td><td><tt>"
                + PropertyValue.toStringRepresentation( thePropertyType.getIsReadOnly(), ModelPrimitivesStringRepresentation.TEXT_HTML )
                + "</tt></td></tr>" );
        w.println( "      *  <tr><td>SequenceNumber:</td><td><tt>"
                + PropertyValue.toStringRepresentation( thePropertyType.getSequenceNumber(), ModelPrimitivesStringRepresentation.TEXT_HTML )
                + "</tt></td></tr>" );
        generateL10Map(
                thePropertyType.getUserVisibleNameMap(),
                "      *  <tr><td>UserVisibleName:</td><td>",
                "</td></tr>",
                w );
        generateL10Map(
                thePropertyType.getUserVisibleDescriptionMap(),
                "      *  <tr><td>UserVisibleDescription:</td><td>",
                "</td></tr>",
                w );
        w.println( "      * </table>" );
    }

    /**
      * Obtain a PrintWriter to which the code will be written.
      *
      * @param theType the MeshType for whose generated code we want to obtain a PrintWriter
      * @return the PrintWriter for the generated code
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    protected final PrintWriter getCodePrintWriterFor(
            MeshType theType )
        throws
            IOException
    {
        String fileName = getAbsoluteCodeFileNameFor( theType );
        File   theFile = new File( fileName );

        if( ! theFile.getParentFile().exists() ) {
            theFile.getParentFile().mkdirs();
        }

        FileOutputStream theFileStream = new FileOutputStream( theFile );

        return new PrintWriter( theFileStream );
    }

    /**
      * Obtain a relative file name into which the code will be written.
      *
      * @param theType the MeshType for whose generated code we want to obtain the file name
      * @return the relative file name into which the code will be written
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    protected abstract String getRelativeCodeFileNameFor(
            MeshType theType )
        throws
            IOException;

    /**
      * Obtain a PrintWriter to which the JavaDoc will be written.
      *
      * @param theType the MeshType for whose generated JavaDoc we want to obtain a PrintWriter
      * @return the PrintWriter for the generated JavaDoc
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    protected final PrintWriter getJavaDocPrintWriterFor(
            SubjectArea theType )
        throws
            IOException
    {
        String fileName = getAbsoluteJavaDocFileNameFor( theType );
        File   theFile = new File( fileName );

        if( ! theFile.getParentFile().exists() ) {
            theFile.getParentFile().mkdirs();
        }

        FileOutputStream theFileStream = new FileOutputStream( theFile );

        return new PrintWriter( theFileStream );
    }

    /**
      * Obtain a relative file name into which the JavaDoc will be written.
      *
      * @param theSubjectArea the MeshType for whose generated code we want to obtain the file name
      * @return the relative file name into which the code will be written
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    protected abstract String getRelativeJavaDocFileNameFor(
            SubjectArea theSubjectArea )
        throws
            IOException;

    /**
      * Obtain an absolute file name into which the code will be written.
      *
      * @param theType the MeshType for whose generated code we want to obtain the file name
      * @return the file name into which the code will be written
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    protected final String getAbsoluteCodeFileNameFor(
            MeshType theType )
        throws
            IOException
    {
        StringBuffer buf = new StringBuffer( 256 );
        buf.append( theOutputDir );
        buf.append( File.separator );
        buf.append( getRelativeCodeFileNameFor( theType ));
        return buf.toString();
    }

    /**
      * Obtain an absolute file name into which the JavaDoc will be written.
      *
      * @param theType the MeshType for whose generated code we want to obtain the file name
      * @return the file name into which the JavaDoc will be written
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    protected final String getAbsoluteJavaDocFileNameFor(
            SubjectArea theType )
        throws
            IOException
    {
        StringBuffer buf = new StringBuffer( 256 );
        buf.append( theOutputDir );
        buf.append( File.separator );
        buf.append( getRelativeJavaDocFileNameFor( theType ));
        return buf.toString();
    }

    /**
      * Helper function that encodes the name of a SubjectArea with its version.
      *
      * @param theSubjectArea the SubjectArea whose name we want to encode
      * @return the encoded SubjectArea name
      */
    protected static String encodeSubjectAreaWithVersion(
            SubjectArea theSubjectArea )
    {
        StringBuffer ret = new StringBuffer( 64 );
        ret.append( theSubjectArea.getName().value() );
        ret.append( ".V" );
        if( theSubjectArea.getVersionNumber() != null ) {
            ret.append( theSubjectArea.getVersionNumber().value());
        }
        return ret.toString();
    }

    /**
     * A helper that can generate the content of an L10Map in HTML format.
     *
     * @param theMap the L10Map containing the internationalized content
     * @param prefix prefix to prepend to the line
     * @param postfix postfix to append to the line
     * @param w the PrintWriter to write to
     */
    protected final void generateL10Map(
            L10Map      theMap,
            String      prefix,
            String      postfix,
            PrintWriter w )
    {
        if( theMap == null ) {
            return;
        }
        if( theMap.isEmpty() && theMap.getDefault() == null ) {
            return;
        }
        w.print( prefix );
        w.print( "<table><tr><td>default locale:</td><td>" );
        w.print( PropertyValue.toStringRepresentation( theMap.getDefault(), ModelPrimitivesStringRepresentation.TEXT_HTML ));
        w.print( "</td></tr>" );
        Iterator<String> theIter = theMap.keyIterator();
        while( theIter.hasNext() ) {
            String key = theIter.next();
            w.print( "<tr><td>" );
            w.print( key );
            w.print( "</td><td>" );
            w.print( PropertyValue.toStringRepresentation( theMap.getExact( key ), ModelPrimitivesStringRepresentation.TEXT_HTML ));
            w.print( "</td></tr>" );
        }
        w.print( "</table>" );
        w.println( postfix );
    }

    /**
     * Obtain the current date and time in String form.
     *
     * @return date and time in String form
     */
    public static String currentDateTime()
    {
        return theCurrentDateTimeFormat.format( new java.util.Date() );
    }

    /**
      * The output directory of code generation.
      */
    protected File theOutputDir;

    /**
     * Our package name translator to translate with version information.
     */
    protected PackageNameTranslatorWithVersion thePackageNameTranslatorWithVersion
            = new PackageNameTranslatorWithVersion();
            
    /**
      * Our package name translator to translate without version information.
      */
    protected PackageNameTranslatorWithoutVersion thePackageNameTranslatorWithoutVersion
            = new PackageNameTranslatorWithoutVersion();

    /**
      * This notice appears that beginning of every file that is being generated.
      */
    protected static final String legalNotice
            = "//\n"
            + "// This file is part of InfoGrid(tm). You may not use this file except in\n"
            + "// compliance with the InfoGrid license. The InfoGrid license and important\n"
            + "// disclaimers are contained in the file LICENSE.InfoGrid.txt that you should\n"
            + "// have received with InfoGrid. If you have not received LICENSE.InfoGrid.txt\n"
            + "// or you do not consent to all aspects of the license and the disclaimers,\n"
            + "// no license is granted; do not use this file.\n"
            + "// \n"
            + "// For more information about InfoGrid go to http://infogrid.org/\n"
            + "//\n"
            + "// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst\n"
            + "// All rights reserved.\n"
            + "//\n";

    /**
     * Determines how date/time stamps are formatted in the generated code.
     */
    protected static final DateFormat theCurrentDateTimeFormat = new SimpleDateFormat(
            "EEE, yyyy-MM-dd HH:mm:ss Z" );
}

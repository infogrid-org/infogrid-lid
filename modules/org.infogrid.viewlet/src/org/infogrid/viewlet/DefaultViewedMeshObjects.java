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

package org.infogrid.viewlet;

import org.infogrid.mesh.MeshObject;
import org.infogrid.util.StringHelper;

import java.util.Map;

/**
 * This is the default implementation of ViewedMeshObjects. This is useful for
 * a wide variety of Viewlets.
 */
public class DefaultViewedMeshObjects
        implements
            ViewedMeshObjects
{
    /**
      * Constructor. Initializes to empty content.
      *
      * @param v the Viewlet that we belong to.
      */
    public DefaultViewedMeshObjects(
            Viewlet v )
    {
        theViewlet = v;
    }

    /**
     * Through this method, the Viewlet that this object belongs to updates this object.
     *
     * @param subject the new subject of the Viewlet
     * @param subjectParameters the parameters of the newly selected subject, if any
     * @param viewletParameters the parameters of the Viewlet, if any
     */
    public void update(
            MeshObject         subject,
            Map<String,Object> subjectParameters,
            Map<String,Object> viewletParameters )
    {
        theSubject           = subject;
        theSubjectParameters = subjectParameters;
        theViewletParameters = viewletParameters;
    }

    /**
     * Through this convenience method, the Viewlet that this object belongs to updates this object.
     *
     * @param newObjectsToView the new objects accepted to be viewed by the Viewlet
     */
    public void updateFrom(
            MeshObjectsToView newObjectsToView )
    {
        update( newObjectsToView.getSubject(),
                newObjectsToView.getSubjectParameters(),
                newObjectsToView.getViewletParameters() );
    }

    /**
     * Obtain the current subject of the Viewlet. As long as the Viewlet
     * displays any information whatsoever, this is non-null.
     *
     * @return the subject MeshObject
     */
    public final MeshObject getSubject()
    {
        return theSubject;
    }

    /**
     * Obtain the parameters for the subject.
     *
     * @return the parameters for the subject, if any.
     */
    public Map<String,Object> getSubjectParameters()
    {
        return theSubjectParameters;
    }

    /**
      * Obtain the Viewlet by which these objects are viewed.
      *
      * @return the Viewlet by which these objects are viewed.
      */
    public Viewlet getViewlet()
    {
        return theViewlet;
    }

    /**
     * Obtain the parameters of the viewing Viewlet.
     *
     * @return the parameters of the viewing Viewlet, if any.
     */
    public Map<String,Object> getViewletParameters()
    {
        return theViewletParameters;
    }

    /**
     * Convert to String, for debugging.
     *
     * @return String representation of this object
     */
    @Override
    public String toString()
    {
        return StringHelper.objectLogString(
                this,
                new String [] {
                    "subject",
                    "viewlet" },
                new Object [] {
                    theSubject,
                    theViewlet } );
    }

    /**
     * The Viewlet that this object belongs to.
     */
    protected Viewlet theViewlet;

    /**
     * The current subject of the Viewlet.
     */
    protected MeshObject theSubject;

    /**
     * The parameters for the subject, if any.
     */
    protected Map<String,Object> theSubjectParameters;

    /**
     * The Viewlet parameters, if any.
     */
    protected Map<String,Object> theViewletParameters;
}

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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.jee.taglib.logic;

import javax.servlet.jsp.JspException;
import org.infogrid.jee.taglib.IgnoreException;
import org.infogrid.jee.taglib.rest.AbstractRestInfoGridBodyTag;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.model.traversal.TraversalSpecification;

/**
 * <p>Factors out common functionality of tags testing the relationship of one MeshObject
 *    to another.</p>
 */
public abstract class AbstractRelatedTag
    extends
        AbstractRestInfoGridBodyTag
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     */
    protected AbstractRelatedTag()
    {
        // noop
    }

    /**
     * Initialize all default values. To be invoked by subclasses.
     */
    @Override
    protected void initializeToDefaults()
    {
        theMeshObjectName             = null;
        theTraversalSpecification     = null;
        theTraversalSpecificationName = null;
        theNeighborName               = null;

        super.initializeToDefaults();
    }

    /**
     * Obtain value of the meshObjectName property.
     *
     * @return value of the meshObjectName property
     * @see #setMeshObjectName
     */
    public final String getMeshObjectName()
    {
        return theMeshObjectName;
    }

    /**
     * Set value of the meshObjectName property.
     *
     * @param newValue new value of the meshObjectName property
     * @see #getMeshObjectName
     */
    public final void setMeshObjectName(
            String newValue )
    {
        theMeshObjectName = newValue;
    }

    /**
     * Obtain value of the traversalSpecification property.
     *
     * @return value of the traversalSpecification property
     * @see #setTraversalSpecification
     */
    public final String getTraversalSpecification()
    {
        return theTraversalSpecification;
    }

    /**
     * Set value of the traversalSpecification property.
     *
     * @param newValue new value of the traversalSpecification property
     * @see #getTraversalSpecification
     */
    public final void setTraversalSpecification(
            String newValue )
    {
        theTraversalSpecification = newValue;
    }

    /**
     * Obtain value of the traversalSpecificationName property.
     *
     * @return value of the traversalSpecificationName property
     * @see #setTraversalSpecificationName
     */
    public final String getTraversalSpecificationName()
    {
        return theTraversalSpecification;
    }

    /**
     * Set value of the traversalSpecificationName property.
     *
     * @param newValue new value of the traversalSpecificationName property
     * @see #getTraversalSpecificationName
     */
    public final void setTraversalSpecificationName(
            String newValue )
    {
        theTraversalSpecification = newValue;
    }

    /**
     * Obtain value of the neighborName property.
     *
     * @return value of the neighborName property
     * @see #setNeighborName
     */
    public final String getNeighborName()
    {
        return theNeighborName;
    }

    /**
     * Set value of the neighborName property.
     *
     * @param newValue new value of the neighborName property
     * @see #getNeighborName
     */
    public final void setNeighborName(
            String newValue )
    {
        theNeighborName = newValue;
    }

    /**
     * Evaluatate the condition. If it returns true, the content of this tag is processed.
     *
     * @return true in order to output the Nodes contained in this Node.
     * @throws JspException thrown if an evaluation error occurred
     * @throws IgnoreException thrown to abort processing without an error
     */
    protected boolean evaluateTest()
        throws
            JspException,
            IgnoreException
    {
        MeshObject start    = (MeshObject) lookupOrThrow( theMeshObjectName );
        MeshObject neighbor = (MeshObject) lookupOrThrow( theNeighborName );

        TraversalSpecification spec;
        if( theTraversalSpecification != null ) {
            if( theTraversalSpecificationName != null ) {
                throw new JspException( "Must specify either traversalSpecification or traversalSpecificationName, not both" );
            }
            spec = findTraversalSpecificationOrThrow( theTraversalSpecification );
        } else if( theTraversalSpecificationName != null ) {
            spec = (TraversalSpecification) lookupOrThrow( theTraversalSpecificationName );
        } else {
            throw new JspException( "Must specify either traversalSpecification or traversalSpecificationName" );
        }

        MeshObjectSet ret = start.traverse( spec );
        if( ret.contains( neighbor )) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * String containing the name of the bean that is the MeshObject whose property is considered in the test.
     */
    protected String theMeshObjectName;

    /**
     * String containing the external form of a TraversalSpecification. This is mutually exclusive with theTraversalSpecificationName.
     */
    protected String theTraversalSpecification;

    /**
     * String containing the name of the bean that is the TraversalSpecification. This is mutually exclusive with theTraversalSpecification.
     */
    protected String theTraversalSpecificationName;

    /**
     * String containing the name of the bean that is the to-be-evaluated neighbor MeshObject.
     */
    protected String theNeighborName;
}

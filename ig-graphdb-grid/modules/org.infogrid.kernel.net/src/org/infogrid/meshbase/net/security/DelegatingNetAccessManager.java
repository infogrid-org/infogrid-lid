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

package org.infogrid.meshbase.net.security;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.net.security.AccessLocallyNotPermittedException;
import org.infogrid.meshbase.net.NetMeshBase;
import org.infogrid.meshbase.net.NetMeshObjectAccessSpecification;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.security.IdentityChangeException;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.RoleType;

/**
 * Makes it easy to create a NetAccessManager by delegating to another AccessManager.
 * By default, accessLocally is prohibited.
 */
public class DelegatingNetAccessManager
        implements
            NetAccessManager
{
    /**
     * Factory method.
     *
     * @param delegate the AccessManager to delegate to
     * @return the created DelegatingNetAccessManager
     */
    public static DelegatingNetAccessManager create(
            AccessManager delegate )
    {
        return new DelegatingNetAccessManager( delegate );
    }

    /**
     * Constructor.
     *
     * @param delegate the AccessManager to delegate to
     */
    protected DelegatingNetAccessManager(
            AccessManager delegate )
    {
        theDelegate = delegate;
    }
    
    /**
     * Determine the identity of the caller. This may return null, indicating that
     * the caller is anonymous.
     *
     * @return the identity of the caller, or null.
     * @see #setCaller
     * @see #unsetCaller
     */
    public MeshObject getCaller()
    {
        return theDelegate.getCaller();
    }

    /**
     * Determine whether the current Thread has super user privileges.
     *
     * @return true if the current Thread has super user privileges.
     */
    public boolean isSu()
    {
        return theDelegate.isSu();
    }

    /**
     * Set the identity of the caller on this Thread. This will unset any previous
     * identity set on this Thread. Generally, the sequence of invocation should be:
     * <pre>
     * try {
     *     setCaller( theCaller );
     *     performWork();
     * } finally {
     *     unsetCaller();
     * }
     * </pre>
     *
     * @param caller the caller, or null if anonymous
     * @return the previously set caller, if any
     * @see #getCaller
     * @see #unsetCaller
     */
    public MeshObject setCaller(
            MeshObject caller )
        throws
            IdentityChangeException
    {
        return theDelegate.setCaller( caller );
    }

    /**
     * Unset the identity of the caller on this Thread. This is called when the caller
     * is done, for example.
     *
     * @return the previously set caller, if any
     * @see #getCaller
     * @see #setCaller
     */
    public MeshObject unsetCaller()
        throws
            IdentityChangeException
    {
        return theDelegate.unsetCaller();
    }

    /**
     * Make the current Thread have super-user rights. This is very similar to common
     * operating-systems calls.
     */
    public void sudo()
        throws
            IdentityChangeException
    {
        theDelegate.sudo();
    }

    /**
     * Release super-user rights from the current Thread. If the current Thread does not
     * have super-user rights, nothing happens.
     */
    public void sudone()
    {
        theDelegate.sudone();
    }
    
    /**
     * Assign the second MeshObject to be the owner of the first MeshObject. This
     * must only be called if the current Thread has an open Transaction.
     *
     * @param toBeOwned the MeshObject to be owned by the new owner
     * @param newOwner the MeshObject that is the new owner.
     * @throws TransactionException thrown if this is invoked outside of proper transaction boundaries
     */
    public void assignOwner(
            MeshObject toBeOwned,
            MeshObject newOwner )
        throws
            TransactionException
    {
        theDelegate.assignOwner( toBeOwned, newOwner );
    }
    
    /**
     * Check whether it is permitted to semantically create a MeshObject with the provided
     * MeshObjectIdentifier.
     * 
     * @param identifier the MeshObjectIdentifier
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedCreate(
            MeshObjectIdentifier identifier )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedCreate( identifier );
    }
    
    /**
     * Check whether it is permitted to set a MeshObject's auto-delete time to the given value.
     *
     * @param obj the MeshObject
     * @param newValue the proposed new value for the auto-delete time
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedSetTimeExpires(
            MeshObject obj,
            long       newValue )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedSetTimeExpires( obj, newValue );
    }
    
    /**
     * Check whether it is permitted to set a MeshObject's given property to the given
     * value.
     *
     * @param obj the MeshObject
     * @param thePropertyType the PropertyType identifing the property to be modified
     * @param newValue the proposed new value for the property
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedSetProperty(
            MeshObject    obj,
            PropertyType  thePropertyType,
            PropertyValue newValue )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedSetProperty( obj, thePropertyType, newValue );
    }
    
    /**
     * Check whether it is permitted to obtain a MeshObject's given property.
     *
     * @param obj the MeshObject
     * @param thePropertyType the PropertyType identifing the property to be read
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedGetProperty(
            MeshObject   obj,
            PropertyType thePropertyType )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedGetProperty( obj, thePropertyType );
    }

    /**
     * Check whether it is permitted to determine whether or not a MeshObject is blessed with
     * the given type.
     * 
     * @param obj the MeshObject
     * @param type the EntityType whose blessing we wish to check
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedBlessedBy(
            MeshObject obj,
            EntityType type )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedBlessedBy( obj, type );
    }

    /**
     * Check whether it is permitted to bless a MeshObject with the given EntityTypes.
     * 
     * @param obj the MeshObject
     * @param types the EntityTypes with which to bless
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedBless(
            MeshObject    obj,
            EntityType [] types )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedBless( obj, types );
    }

    /**
     * Check whether it is permitted to unbless a MeshObject from the given EntityTypes.
     * 
     * @param obj the MeshObject
     * @param types the EntityTypes from which to unbless
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedUnbless(
            MeshObject    obj,
            EntityType [] types )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedUnbless( obj, types );
    }

    /**
     * Check whether it is permitted to bless the relationship to the neighbor with the
     * provided RoleTypes.
     *
     * @param obj the MeshObject
     * @param thisEnds the RoleTypes to bless the relationship with
     * @param neighborIdentifier identifier of the neighbor to which this MeshObject is related
     * @param neighbor neighbor to which this MeshObject is related, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedBless(
            MeshObject           obj,
            RoleType []          thisEnds,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedBless( obj, thisEnds, neighborIdentifier, neighbor );
    }

    /**
     * Check whether it is permitted to unbless the relationship to the neighbor from the
     * provided RoleTypes.
     *
     * @param obj the MeshObject
     * @param thisEnds the RoleTypes to unbless the relationship from
     * @param neighborIdentifier identifier of the neighbor to which this MeshObject is related
     * @param neighbor neighbor to which this MeshObject is related, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedUnbless(
            MeshObject           obj,
            RoleType []          thisEnds,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedUnbless( obj, thisEnds, neighborIdentifier, neighbor );
    }

    /**
     * Check whether it is permitted to traverse the given RoleType from this MeshObject to the
     * given MeshObject.
     *
     * @param obj the MeshObject
     * @param toTraverse the RoleType to traverse
     * @param neighborIdentifier identifier of the neighbor to which the traversal leads
     * @param neighbor neighbor to which this traversal leads
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedTraversal(
            MeshObject           obj,
            RoleType             toTraverse,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedTraversal( obj, toTraverse, neighborIdentifier, neighbor );
    }

    /**
     * Check whether it is permitted to make one MeshObject equivalent to another.
     *
     * @param one the first MeshObject
     * @param twoIdentifier identifier of the second MeshObject
     * @param two the second MeshObject, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedAddAsEquivalent(
            MeshObject           one,
            MeshObjectIdentifier twoIdentifier,
            MeshObject           two )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedAddAsEquivalent( one, twoIdentifier, two );
    }

    /**
     * Check whether it is permitted to remove a MeshObject from the equivalence set
     * it is currently a member of.
     * Subclasses may override this.
     * 
     * @param obj the MeshObject to remove
     * @param roleTypesToAsk the RoleTypes to ask
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedRemoveAsEquivalent(
            MeshObject  obj,
            RoleType [] roleTypesToAsk )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedRemoveAsEquivalent( obj, roleTypesToAsk );        
    }
    
    /**
     * Check whether it is permitted to delete this MeshObject. This checks both whether the
     * MeshObject itself may be deleted, and whether the relationships it participates in may
     * be deleted (which in turn depends on whether the relationships may be unblessed).
     *
     * @param obj the MeshObject
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedDelete(
            MeshObject obj )
        throws
            NotPermittedException
    {
        theDelegate.checkPermittedDelete( obj );
    }

    /**
     * Check whether it is permitted to perform an accessLocally operation with the
     * provided NetMeshObjectAccessSpecification.
     *
     * @param mb the MeshBase in which the Exception occurred
     * @param failedPaths the access path that was used
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedAccessLocally(
            NetMeshBase                         mb,
            NetMeshObjectAccessSpecification [] failedPaths )
        throws
            NotPermittedException
    {
        throw new AccessLocallyNotPermittedException( mb, mb.getIdentifier(), failedPaths );
    }
    
    /**
     * The AccessManager to delegate to.
     */
    protected AccessManager theDelegate;
}
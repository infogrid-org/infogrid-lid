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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.lid.local;

import java.util.Map;
import org.infogrid.lid.LidHasIdentifierFinder;
import org.infogrid.lid.credential.LidCredentialType;
import org.infogrid.util.Identifier;
import org.infogrid.util.InvalidIdentifierException;
import org.infogrid.util.http.SaneRequest;

/**
 * Manages locally provisioned identities.
 */
public interface LidLocalPersonaManager
        extends
            LidHasIdentifierFinder
{
    /**
     * Create a LidLocalPersona.
     *
     * @param identifier the identifier for the to-be-created LidLocalPersona
     * @param attributes the attributes for the to-be-created LidLocalPersona
     * @param credentials the credentials for the to-be-created LidLocalPersona
     * @return the LocalPersona that was created
     * @throws LidLocalPersonaExistsAlreadyException thrown if a LidLocalPersona with this identifier exists already
     * @throws UnsupportedOperationException thrown if this LidLocalPersonaManager does not permit the creation of new LidLocalPersonas
     */
    public LidLocalPersona createLocalPersona(
            Identifier                    identifier,
            Map<String,String>            attributes,
            Map<LidCredentialType,String> credentials )
        throws
            LidLocalPersonaExistsAlreadyException,
            UnsupportedOperationException;

    /**
     * Obtain a LidLocalPersona, given its identifier.
     *
     * @param identifier the identifier for which the LidLocalPersona will be retrieved
     * @return the found LidLocalPersona
     * @throws LidLocalPersonaUnknownException thrown if no LidLocalPersona exists with this identifier
     * @throws InvalidIdentifierException thrown if an invalid Identifier was provided
     */
    public abstract LidLocalPersona find(
            Identifier identifier )
        throws
            LidLocalPersonaUnknownException,
            InvalidIdentifierException;

    /**
     * Find the LidResource, or null, which in this case is a LidLocalPersona.
     * 
     * @param request the incoming request
     * @return the found LidResource, or null
     * @throws LidLocalPersonaUnknownException thrown if no LidLocalPersona exists with this identifier
     * @throws InvalidIdentifierException thrown if an invalid Identifier was provided
     */
    public LidLocalPersona findFromRequest(
            SaneRequest request )
        throws
            LidLocalPersonaUnknownException,
            InvalidIdentifierException;

    /**
     * Delete a LidLocalPersona, given its identifier.
     * 
     * @param identifier the identifier of the LidLocalPersona that will be deleted
     * @throws UnsupportedOperationException thrown if this LidLocalPersonaManager does not permit the deletion of LidLocalPersonas
     * @throws LidLocalPersonaUnknownException thrown if no LidLocalPersona exists with this identifier
     */
    public abstract void delete(
            Identifier identifier )
        throws
            UnsupportedOperationException,
            LidLocalPersonaUnknownException;
}

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

package org.infogrid.jee.taglib.meshbase.net;

import org.infogrid.meshbase.net.Proxy;

import org.infogrid.jee.taglib.AbstractInfoGridBodyTag;
import org.infogrid.jee.taglib.IgnoreException;
import org.infogrid.jee.taglib.net.InfoGridNetJspUtils;

import javax.servlet.jsp.JspException;

/**
 * <p>Tag that displays the identifier of the PartnerNetMeshBase of a Proxy.</p>
 */
public class ProxyIdentifierTag
    extends
        AbstractInfoGridBodyTag
{
    /**
     * Constructor.
     */
    public ProxyIdentifierTag()
    {
        // noop
    }

    /**
     * Initialize.
     */
    @Override
    protected void initializeToDefaults()
    {
        theProxyName            = null;
        theStringRepresentation = null;
        theMaxLength            = -1;

        super.initializeToDefaults();
    }

    /**
     * Obtain value of the proxyName property.
     * 
     * @return value of the proxyName property
     * @see #setProxyName
     */
    public String getProxyName()
    {
        return theProxyName;
    }

    /**
     * Set value of the proxyName property.
     * 
     * @param newValue new value of the proxyName property
     * @see #getProxyName
     */
    public void setProxyName(
            String newValue )
    {
        theProxyName = newValue;
    }

    /**
     * Obtain value of the stringRepresentation property.
     *
     * @return value of the stringRepresentation property
     * @see #setStringRepresentation
     */
    public String getStringRepresentation()
    {
        return theStringRepresentation;
    }

    /**
     * Set value of the stringRepresentation property.
     *
     * @param newValue new value of the stringRepresentation property
     * @see #getStringRepresentation
     */
    public void setStringRepresentation(
            String newValue )
    {
        theStringRepresentation = newValue;
    }

    /**
     * Obtain value of the maxLength property.
     *
     * @return value of the maxLength property
     * @see #setMaxLength
     */
    public int getMaxLength()
    {
        return theMaxLength;
    }

    /**
     * Set value of the maxLength property.
     *
     * @param newValue new value of the maxLength property
     */
    public void setMaxLength(
            int newValue )
    {
        theMaxLength = newValue;
    }
    
    /**
     * Do the start tag operation.
     *
     * @return evaluate or skip body
     * @throws JspException thrown if an evaluation error occurred
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    protected int realDoStartTag()
        throws
            JspException,
            IgnoreException
    {
        Proxy p = (Proxy) lookupOrThrow( theProxyName );
        
        String text = InfoGridNetJspUtils.formatProxyIdentifierStart( pageContext, p, theStringRepresentation );
               text = InfoGridNetJspUtils.potentiallyShorten( text, theMaxLength );
        
        print( text );

        return EVAL_BODY_INCLUDE;
    }

    /**
     * Do the end tag operation.
     *
     * @return evaluate or skip page
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    @Override
    protected int realDoEndTag()
        throws
            JspException,
            IgnoreException
    {
        Proxy p = (Proxy) lookupOrThrow( theProxyName );
        
        String text = InfoGridNetJspUtils.formatProxyIdentifierEnd( pageContext, p, theStringRepresentation );
               text = InfoGridNetJspUtils.potentiallyShorten( text, theMaxLength );

        print( text );

        return EVAL_PAGE;
    }
    
    /**
     * Name of the bean that holds the Proxy.
     */
    protected String theProxyName;
    
    /**
     * Name of the String representation.
     */
    protected String theStringRepresentation;

    /**
     * The maximum length of an emitted String.
     */
    protected int theMaxLength;
}

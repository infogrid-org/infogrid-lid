<candy:overlay id="org-infogrid-jee-shell-http-HttpShellVerb-setProperty">
 <u:safeForm method="post" action="${Viewlet.postUrl}">
  <h2>Set a property of a MeshObject</h2>
  <table>
   <tr>
    <td class="label">MeshObject:</td>
    <td>
     <input class="subject" name="mesh.subject" size="32" readonly="readonly" />
     <input type="hidden" name="mesh.verb" value="setProperty" />
    </td>
   </tr>
   <tr>
    <td class="label">Property Type:</td>
    <td><input class="propertytype" name="mesh.propertytype" size="32" /></td>
   </tr>
   <tr>
    <td class="label">Property Value:</td>
    <td><input class="propertyvalue" name="mesh.propertyvalue" size="32" /></td>
   </tr>
   <tr>
    <td colspan="2">
     <table class="dialog-buttons">
      <tr>
       <td><input type="submit" value="Set property" /></td>
       <td><a href="javascript:overlay_hide( 'org-infogrid-jee-shell-http-HttpShellVerb-setProperty' )">Cancel</a></td>
      </tr>
     </table>
    </td>
   </tr>
  </table>
 </u:safeForm>
</candy:overlay>
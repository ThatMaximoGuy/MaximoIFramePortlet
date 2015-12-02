<%@ include file="../common/simpleheader.jsp"%>
<%
String layoutId =  component.getProperty("layoutid");
PortletDataInstance portletControl = (PortletDataInstance)control;
PortletStateImpl portletStateManager = portletControl.getStateManager();

//if the portlet has not been loaded ever call this.
//once its loaded we do not clear the flag
if(portletStateManager.isPortletNotLoaded(component)) {
%><%@ include file="portletloader.jsp"%><%
} else {
	synchronized (portletControl.getAppBean().getPortletLoadingSync())
	{
		boolean stateChanged = portletStateManager.isPortletStateChanged();
		//if just porlet was minimised  then don't re render as there is no need to redraw
		//but this flag had to be turned on when these events were fired so that jsp could be hit
		if(stateChanged) {
			portletControl.setChangedFlag(false);
		}
		if(portletStateManager.wasPortletLoadCalled())
		{	
			holderId="portletbody_"+layoutId;	
%>
<div>Hello there everybody.</div>
<!--  portlet content here -->
<script>finishPortlet("<%=layoutId%>");</script><%="]]>"%></component>
<%			portletStateManager.setStateLoaded();
		} else {
			///If the portlet state is changed then fire the javascript to toggle its state
			if(portletStateManager.isPortletStateChanged())
			{
				portletStateManager.setPortletStateChanged(false);
	%>	<component vis="true"  id="<%=id%>_holder"<%=compType%>><%="<![CDATA["%>
			<script>
				var portletContent = document.getElementById("portletbody_<%= component.getProperty("layoutid") %>_outer");
				hideShowElement("portletbody_<%=component.getProperty("layoutid")%>_outer",(portletContent.style.display =='none'));
			</script>
		<%="]]>"%></component>	
	<%		}
		}
	}
}
%><%@ include file="../common/componentfooter.jsp" %>





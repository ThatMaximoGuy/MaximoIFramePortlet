#!/bin/sh

# iifp-install.sh -- 
#	Runs com.interlocsolutions.maximo.tools.IFramePortletEnvSetup
#

pathToScript=`dirname %0`
. $pathToScript/commonenv.sh

../java/jre/bin/java -classpath .:./classes:$MAXIMO_CLASSPATH:./lib/jsap.jar:./lib/jaxen.jar  com.interlocsolutions.maximo.tools.InstallerEnvSetup "$*"
internal/runscriptfile.sh -cisiframe -fiframe_001
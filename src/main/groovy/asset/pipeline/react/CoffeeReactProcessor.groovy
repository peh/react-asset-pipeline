/*
* Copyright 2014 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package asset.pipeline.react

import asset.pipeline.AbstractProcessor
import asset.pipeline.AssetCompiler
import asset.pipeline.AssetFile
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

// Will attempt to use Node.JS cofee-react if it is available on the system path.
// TODO: Implement support for Mozilla Rhino using coffee-react-transform
class CoffeeReactProcessor extends AbstractProcessor {

    static Boolean NODE_SUPPORTED
    Scriptable globalScope
    ClassLoader classLoader

    CoffeeReactProcessor(AssetCompiler precompiler) {
        super(precompiler)
        if (!isNodeSupported()) {
            try {
                Context.exit()
            } catch (IllegalStateException ignored) {
            }
            throw new Exception("Initialization failed - Node.js is required for coffee-react transformation.")
        }
    }

    /**
     * Processes an input string from a given AssetFile implementation of cjsx and converts it to coffeescript
     * @param input String input cjsx script text to be converted to coffeescript
     * @param AssetFile instance of the asset file from which this file came from. Not actually used currently for this implementation.
     * @return String of compiled coffeescript
     */
    String process(String input, AssetFile assetFile) {
        if (isNodeSupported()) {
            return processWithNode(input, assetFile)
        } else {
            Context.exit()
            throw new Exception("Initialization failed - Node.js is required for coffee-react transformation.")
        }
    }

    /**
     * Processes an input string of cjsx using node.js (Don't use directly)
     * @param input String input cjsx script text to be converted to coffeescript
     * @param AssetFile instance of the asset file from which this file came from.
     * @return String of compiled coffeescript
     */
    def processWithNode(input, assetFile) {
        def nodeProcess
        def output = new StringBuilder()
        def err = new StringBuilder()

        try {
            def command = "${isWindows() ? 'cmd /c ' : ''}cjsx -csp"
            nodeProcess = command.execute()
            nodeProcess.getOutputStream().write(input.bytes)
            nodeProcess.getOutputStream().flush()
            nodeProcess.getOutputStream().close()
            nodeProcess.waitForProcessOutput(output, err)
            if (err) {
                throw new Exception(err.toString())
            }
            return output.toString()
        } catch (Exception e) {
            throw new Exception("""
			Node.js transformation of cjsx to coffeescript failed.
			$e
			""")
        }
    }

    /**
     * Determins if this is on a windows platform or not (used for node system path)
     * @return Boolean true if this is a windows machine
     */
    Boolean isWindows() {
        String osName = System.getProperty("os.name");
        return (osName != null && osName.contains("Windows"))
    }

    /**
     * Determins if Node is supported on the System path
     * @return Boolean true if Node.js is supported on the system path
     */
    Boolean isNodeSupported() {
        if (NODE_SUPPORTED == null) {
            def nodeProcess
            def output = new StringBuilder()
            def err = new StringBuilder()

            try {
                def command = "${isWindows() ? 'cmd /c ' : ''}cjsx -v"
                nodeProcess = command.execute()
                nodeProcess.waitForProcessOutput(output, err)
                if (err) {
                    NODE_SUPPORTED = false
                } else {
                    NODE_SUPPORTED = true
                }
            } catch (Exception ignored) {
                NODE_SUPPORTED = false
            }
        }
        return NODE_SUPPORTED
    }

}

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

import spock.lang.Ignore
import spock.lang.Specification

/**
 * @author Tom Crossland
 */
class CoffeeReactProcessorSpec extends Specification {
    @Ignore
    // TODO: Rhino support currently not implemented
    void "should compile cjsx into coffee using rhino"() {
        given:
        def cjsxScript = '-> <a />'
        CoffeeReactProcessor.NODE_SUPPORTED = false
        def processor = new CoffeeReactProcessor()

        when:
        def output = processor.process(cjsxScript, null)

        then:
        output.contains('React.createElement')
    }

    void "should compile cjsx into coffee using node"() {
        given:
        def cjsxScript = '-> <a />'
        CoffeeReactProcessor.NODE_SUPPORTED = true
        def processor = new CoffeeReactProcessor()

        when:
        def output = processor.process(cjsxScript, null)

        then:
        output.contains('React.createElement')
    }
}

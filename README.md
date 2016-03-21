# AppVersionControl
Application Version Control (Update) Android's Library for Custom Server based API

## Installation

    To be upload to jcenter

## Implementation

Extends Application in Android with `AppVersionApplication`

    public YOUR_APP_CLASS extends AppVersionApplication
    
Don't forget to add application to `AndroidManifest.xml`
   
    android:name=".YOUR_APP_CLASS"

Create AppVersionController in your `onCreate()` this fashion

    AppVersionController appVersionController;
    appVersionController = new AppVersionController((AppVersionApplication)getApplication(),this, NetworkConstant.API_URL,pathUrl,"1.0.0","com.package.example");

Start the version controller in your `onResume()` or `onResumeFragments()` this fashion

    appVersionController.start();
    
Start the version controller in your `onPause` this fashion

    appVersionController.stop();
    
To log your link and other thing set `CLog.setAllowLog(boolean)`

    CLog.setAllowLog(true);
    
## License
	Copyright 2016 Intouch Marsvongpragorn
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	   http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
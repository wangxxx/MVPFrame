# MVPFrame
Step 1.  
project.gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. 
model.gradle
Add the dependency
	dependencies {
	        implementation 'com.github.wangxxx:MVPFrame:1.0.0'
	}

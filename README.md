1. build with   
```mvn clean package```  
   
2. put codes.yml next to jar files in formant:  
 totp_<some_name_1>: <secret_code_1>
 totp_<some_name_2>: <secret_code_2>
 ...  
3. launch java -jar totp-get.jar  

As a result it will print TOTP codes for you in terminal as soon as they refresh

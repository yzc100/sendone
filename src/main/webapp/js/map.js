function Map(){
   this.map    =  new Object();
   this.length = 0;
  
   this.size = function(){
       return this.length;
   }
  
   this.put = function(key, value){
     
      if( !this.map[key])
        {
             ++this.length;
        }
     
      this.map[key] = value;
    
   }
  
   this.remove = function(key){
      
       if(this.map[key])
      {
         
          --this.length;
        return delete this.map[key];
      }
      else
      {
          return false;
      }
   }
  
   this.containsKey = function(key){
   
     return this.map[key] ? true:false;
  
   }
   
   this.get = function(key){   
 
      return this.map[key] ? this.map[key]:null;
 
   }


 this.inspect=function(){   
     var str = '';
    
     for(var each in this.map)
     {
          str+= '\n'+ each + '  Value:'+ this.map[each];
     }
    
     return str;
   }
 this.toJSON = function(){
	 var str ='{';
	 for(var each in this.map){
		 str+=each+":\""+this.map[each]+"\",";
	 }
	 return str.substring(0,str.length-1)+"}";
 }
 this.toParamsJSON = function(){
	 var str ="data={";
	 for(var each in this.map){
		 str+="\""+each+"\":\""+this.map[each]+"\",";
	 }
	 return str.substring(0,str.length-1)+"}";
 }
    
}
prérequis:
	-tomcat 8
	-java 17

instructions:
	-ny anaran'ny projet atao anaty "init-param" ao am "web.xml":
		-soloina amin'ny anara'ilay projet ny eo amin'ilay hoe "Anarana_projet" anaty <param-value> eo ambany
		-copier-na ao anaty web.xml ny "init-param" eo ambany

			<init-param>
				<param-name> projectName </param-name>
				<param-value> Anarana_projet </param-value>
			</init-param> 
	
	-rehefa manao controller anaty classe iray:
		-mila importena ny classe : annotation.Url;
		-mila importena ny classe : modelView.ModelView;
		-ny fonction controller dia anotena @Url( url = " url_miantso_ilay fonction ")
		-ny fonction controller dia tsy maintsy mamerina ModelView 
		-ny anaran'ny page tokony andehanana rehefa miantso ilay fonction dia atao argument an'ny constructeur ModelView: new ModelView( "anarana_page.jsp" )

	-rehefa manao compilation
		-mila atao anaty ClassPath ilay "fw.jar"
		-asina option " -parameters "  ohatra: javac -parameters -d . *.java

	-rehefa manao formulaire anaty jsp:
		-mila atao mitovy ny "name" anaty input sy ny anarana argument an'ilay fonction mandray ilay formulaire
	
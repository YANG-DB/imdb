##IMDb Dataset Details 

Each dataset is contained in a gzipped, tab-separated-values (TSV) formatted file in the UTF-8 character set. The first line in each file contains headers that describe what is in each column. A ‘\N’ is used to denote that a particular field is missing or null for that title/name. The available datasets are as follows: 

##title.akas.tsv.gz - 
Contains the following information for titles:

<strong>titleId</strong> (string) - a tconst, an alphanumeric unique identifier of the title<br>
<strong>ordering</strong> (integer) – a number to uniquely identify rows for a given titleId<br>
<strong>title</strong> (string) – the localized title<br>
<strong>region</strong> (string) - the region for this version of the title<br>
<strong>language</strong> (string) - the language of the title<br>
<strong>types</strong> (array) - Enumerated set of attributes for this alternative title. One or more of the following: "alternative", "dvd", "festival", "tv", "video", "working", "original", "imdbDisplay". New values may be added in the future without warning<br>

<br><strong>attributes</strong> (array) - Additional terms to describe this alternative title, not enumerated<br>
<strong>isOriginalTitle</strong> (boolean) – 0: not original title; 1: original title<br>


##title.basics.tsv.gz -
Contains the following information for titles:

<strong>tconst</strong> (string) - alphanumeric unique identifier of the title<br>
<strong>titleType</strong> (string) – the type/format of the title (e.g. movie, short, tvseries, tvepisode, video, etc)<br>
<strong>primaryTitle</strong> (string) – the more popular title / the title used by the filmmakers on promotional materials at the point of release<br>
<br><strong>originalTitle</strong> (string) - original title, in the original language<br>
<strong>isAdult</strong> (boolean) - 0: non-adult title; 1: adult title<br>
<strong>startYear</strong> (YYYY) – represents the release year of a title. In the case of TV Series, it is the series start year<br>
<strong>endYear</strong> (YYYY) – TV Series end year. ‘\N’ for all other title types<br>
<strong>runtimeMinutes</strong> – primary runtime of the title, in minutes<br>
<strong>genres</strong> (string array) – includes up to three genres associated with the title<br>

##title.crew.tsv.gz –
Contains the director and writer information for all the titles in IMDb. Fields include:

<strong>tconst</strong> (string) - alphanumeric unique identifier of the title<br>
<strong>directors</strong> (array of nconsts) - director(s) of the given title<br>
<strong>writers</strong> (array of nconsts) – writer(s) of the given title

##title.episode.tsv.gz –
Contains the tv episode information. Fields include:

<strong>tconst</strong> (string) - alphanumeric identifier of episode<br>
<strong>parentTconst</strong> (string) - alphanumeric identifier of the parent TV Series<br>
<strong>seasonNumber</strong> (integer) – season number the episode belongs to<br>
<strong>episodeNumber</strong> (integer) – episode number of the tconst in the TV series<br>


##title.principals.tsv.gz – 
Contains the principal cast/crew for titles

<strong>tconst</strong> (string) - alphanumeric unique identifier of the title<br>
<strong>ordering</strong> (integer) – a number to uniquely identify rows for a given titleId<br>
<strong>nconst</strong> (string) - alphanumeric unique identifier of the name/person<br>
<strong>category</strong> (string) - the category of job that person was in<br>
<strong>job</strong> (string) - the specific job title if applicable, else '\N'<br>
<strong>characters</strong> (string) - the name of the character played if applicable, else '\N'<br>

##title.ratings.tsv.gz –
Contains the IMDb rating and votes information for titles

<strong>tconst</strong> (string) - alphanumeric unique identifier of the title<br>
<strong>averageRating</strong> – weighted average of all the individual user ratings<br>
<strong>numVotes</strong> - number of votes the title has received

##name.basics.tsv.gz –
Contains the following information for names:

<strong>nconst</strong> (string) - alphanumeric unique identifier of the name/person<br>
<strong>primaryName</strong> (string)– name by which the person is most often credited<br>
<strong>birthYear</strong> – in YYYY format<br>
<strong>deathYear</strong> – in YYYY format if applicable, else '\N'<br>
<strong>primaryProfession</strong> (array of strings)– the top-3 professions of the person<br>
<strong>knownForTitles</strong> (array of tconsts) – titles the person is known for

###The case for Yang.db with imdb

This imdb repository will demonstrate the Knowledge (RDF) ontology, the imdb tables will be ingested into yang.db (E/S)
and will be queries with different cypher patterns.

    
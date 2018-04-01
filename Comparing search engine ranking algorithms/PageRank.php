<?php
// make sure browsers see this page as utf-8 encoded HTML
header('Content-Type: text/html; charset=utf-8');
$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;
$results = false;
if ($query)
{
 // The Apache Solr Client library should be on the include path
 // which is usually most easily accomplished by placing in the
 // same directory as this script ( . or current directory is a default
 // php include path entry in the php.ini)
    require_once('Apache/Solr/Service.php');
 // create a new solr service instance - host, port, and corename
 // path (all defaults in this example)
    $solr = new Apache_Solr_Service('localhost', 8983, '/solr/shreyacore/');
 // if magic quotes is enabled then stripslashes will be needed
    if (get_magic_quotes_gpc() == 1)
    {
        $query = stripslashes($query);
    }
 // in production code you'll always want to use a try /catch for any
 // possible exceptions emitted by searching (i.e. connection
 // problems or a query parsing error)
    try
    {   
        if($_GET["sort"]=="PAGERANK"){
            $additionalParameters = array('sort' => 'pageRankFile desc');    
        }else{
            $additionalParameters = array('sort' => '');
        }
         
        //$results = $solr->search($query, $start, $rows, $additionalParameters);
 
        $results = $solr->search($query, 0, $limit, $additionalParameters);
    }
    catch (Exception $e)
    {
 // in production you'd probably log or email this error to an admin
 // and then show a special message to the user but for this example
 // we're going to show the full exception
        die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
    }
}
?>
<html>
<head>
    <title>Washington Post Search Bar</title>
</head>
<body>
<center>
    <form accept-charset="utf-8" method="get">
        <label for="q"><b><p style="color:blue;">Washington Post Search:</p></b></label>
        <br>
        <input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>
        <br>
        <input type="radio" name="sort" value="SOLR" <?php if(isset($_REQUEST['sort']) && $_GET["sort"]=="SOLR") echo 'checked ="checked"' ?>> SOLR
        <input type="radio" name="sort" value="PAGERANK" <?php if(isset($_REQUEST['sort']) && $_GET["sort"]=="PAGERANK") echo 'checked ="checked"' ?>> PAGERANK<br>
        <br>
        <input type="submit"/>
    </form>

    <?php
// display results
    if ($results)
    {
        $total = (int) $results->response->numFound;
        $start = min(1, $total);
        $end = min($limit, $total);
 
        $inputFile = file("/home/shreya1994/Downloads/WP/WP/WPMap.csv");
 
        foreach ($inputFile as $line) {
            $file = str_getcsv($line);
            $fileUrlMap[$file[0]] = trim($file[1]);
        }
 
        ?>
 
        <div>Results <?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
        <ol>
            <?php
 // iterate result documents
            foreach ($results->response->docs as $doc)
            {
                $title = $doc->title;
                $id = $doc->id;
 
                $key = str_replace("/home/shreya1994/Downloads/WP/WP/WP/","",$id); 
 
                $description = $doc->description;
                //$url = $doc->og_url;
                $url = $fileUrlMap[$key];
                 
                ?>
                    <li><b><a href="<?php echo $url ?>"><?php echo $title ?> </a></b><br>
                    <i><a href="<?php echo $url ?>"><?php echo $url ?></a></i><br>
                    <?php echo $id ?> <br>
                    <?php echo $description ?>
                    </li>
                    <br>
                <?php
            }
            ?>
        </ol>
        <?php
    }
    ?>
</center>
</body>
</html>

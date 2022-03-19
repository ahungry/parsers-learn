  ' this is a comment
  if request.IsFoo and Request.QueryString("blub") = "" then
    request.SomeCallHere
  end if

  request.doSomeStuff

  dim favoriteFood
  set favoriteFood = new FavoriteFoodController

  'another comment
  ' yet another comment
  if request.Person = "" or request.Person = "Visitor" then
  elseif Request.QueryString("blub") <> "" then
  else
    request.RouteTo "example.com"
  end if

    CALL some.StoredProcedure(one, two, three, "Four")

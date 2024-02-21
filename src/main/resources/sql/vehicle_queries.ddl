select *
from manheim_crawler.vehicles
where ( announcements not like '%brand%'
   and announcements not like '%rebuil%'
   and announcements not like '%loss%'
   and announcements not like '%reconstruc%'
   and announcements not like '%salvage%'
   and announcements not like '%struct%'
   and announcements not like '%deploy%'
   and announcements not like '%frame%'
   and announcements not like '%slvg%'
   and announcements not like '%junk%')
  and (announcements like '%trans%'
   or announcements like '%trann%'
   or announcements like '%trn%')
  and (make_model like '%nissan%')					-- NISSAN
  and (year >= 2013 and year <= 2019)
  and (odometer <= 90000)
  and (run_date >= '2021-07-27')
;


select *
from manheim_crawler.vehicles
where ( announcements not like '%brand%'
   and announcements not like '%rebuil%'
   and announcements not like '%loss%'
   and announcements not like '%reconstruc%'
   and announcements not like '%salvage%'
   and announcements not like '%struct%'
   and announcements not like '%deploy%'
   and announcements not like '%frame%'
   and announcements not like '%slvg%'
   and announcements not like '%junk%')
  and (announcements like '%engine%'
   or announcements like '%eng%'
   or announcements like '%timing%')
  and (make_model like '%hyundai%'					-- HYUNDAI, KIA
   or make_model like '%kia%')
  and (year >= 2012 and year <= 2019)
  and (odometer <= 120000)
  and (run_date >= '2021-07-27')
;


select *
from manheim_crawler.vehicles
where ( announcements not like '%brand%'
   and announcements not like '%rebuil%'
   and announcements not like '%loss%'
   and announcements not like '%reconstruc%'
   and announcements not like '%salvage%'
   and announcements not like '%struct%'
   and announcements not like '%deploy%'
   and announcements not like '%frame%'
   and announcements not like '%slvg%'
   and announcements not like '%junk%')
  and (announcements like '%engine%'
   or announcements like '%eng%'
   or announcements like '%timing%'
   or announcements like '%trans%'
   or announcements like '%trann%')
  and (make_model like '%bmw%'
   or make_model like '%mercedes%')			-- MERCEDES + BWM
  and (year >= 2012 and year <= 2019)
  and (odometer <= 120000)
  and (run_date >= '2021-07-27')
;


select *
from manheim_crawler.vehicles
where ( announcements not like '%brand%'
   and announcements not like '%rebuil%'
   and announcements not like '%loss%'
   and announcements not like '%reconstruc%'
   and announcements not like '%salvage%'
   and announcements not like '%struct%'
   and announcements not like '%deploy%'
   and announcements not like '%frame%'
   and announcements not like '%slvg%'
   and announcements not like '%junk%')
  and (announcements like '%fld%'
   or announcements like '%flood%'
   or announcements like '%bio%')					-- FLOOD + BIO
  and (odometer <= 120000)
  and (run_date >= '2021-07-27')
;

set sql_safe_updates = 0;

delete
from manheim_crawler.vehicles
where run_date <= '2021-07-24';
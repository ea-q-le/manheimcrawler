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
  and (make_model like '%nissan%')
  and (year >= 2013 and year <= 2019)
  and (odometer <= 90000)
  and (run_date >= '2020-10-23')
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
   or announcements like '%bio%')
  and (odometer <= 120000)
  and (run_date >= '2020-10-23')
;

set sql_safe_updates = 0;

delete
from manheim_crawler.vehicles
where run_date <= '2020-10-24';
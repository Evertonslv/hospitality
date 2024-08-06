import {HttpEvent, HttpInterceptorFn, HttpResponse} from '@angular/common/http'
import {map} from 'rxjs'

export const ApiInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    map((event: HttpEvent<any>) => {
      if (event instanceof HttpResponse) {
        if (event.body && event.body.data && Number(event.body.code) >= 200 && Number(event.body.code) < 300) {
          return event.clone({ body: event.body.data })
        }
        if (event.body && event.body.message && Number(event.body.code) >= 300) {
          throw new Error(event.body.message)
        }
      }
      return event;
    })
  );
};

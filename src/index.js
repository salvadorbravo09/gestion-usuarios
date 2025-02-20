import httpServer from './config/http.js';
import config from './config/env.js';

const bootstrap = () => {
  httpServer.listen(config.PORT, () => {
    console.log(`Server running on port ${process.env.PORT}`);
  });
};

bootstrap();
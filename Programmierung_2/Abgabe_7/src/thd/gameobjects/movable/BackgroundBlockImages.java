package thd.gameobjects.movable;

class BackgroundBlockImages {
    static final String EDGE = """
            gggg      gggg  gg      gggg
            RRggRR  gggggggggg      ggggRR
            RRggRRggggggggggggggggggggggRR
            RRggRRggggggggggggggggggggggRRgg
            RRggRRggggggggggggggggggggggRRgg
            RRggRRggggggggggggggggggggggRRgggggg
            RRggRRggggggggggggggggggggggRRggRRgg      gggg  gg      gggg
            RRggRRggggggggggggggggggggggRRggRRggRR  gggggg  gg      gggg
            RRggRRggggggggggggggggggggggRRggRRggRRgggggggggggg      ggggRR
            RRggRRggggggggggggggggggggggRRggRRggRRggggggggggggggggggggggRR
              ggRRggggggggggggggggggggggRRggRRggRRggggggggggggggggggggggRRgg
                RRgggggggggggggggggg  ggRRggRRggRRggggggggggggggggggggggRRgg
                  gg  gg  gggggggggg    RRggRRggRRggggggggggggggggggggggRRgg
                            gg  gg        ggRRggRRggggggggggggggggggggggRRgg
                            gg            ggRRggRRggggggggggggggggggggggRRgggg
                                          ggRRggRRggggggggggggggggggggggRRggRR
                                            RRggRRggggggggggggggggggggggRRggRRgggg
                                            RRggRRggggggggggggggggggggggRRggRRggRR          gg
                                              ggRRggggggggggggggggggggggRRggRRggRR      gg  gggg
                                                RRgggggggggggggggggg  ggRRggRRggRRgggggggggggggg  gggggg
                                                  gg  gg  gggggggggg    RRggRRggRRgggggggggggggggggggggggg
                                                            gg  gg        ggRRggRRgggggggggggggggggggggggggg
                                                            gg            ggRRggRRgggggggggggggggggggggggggggggg      gggg  gg      gggg
                                                                          ggRRggRRggggggggggggggggggggggggggRRgg      gggg  gg      gggg
                                                                            RRggRRggggggggggggggggggggggggggRRggRR  ggggggggggggggggggggRR
                                                                            RRggRRggggggggggggggggggggggggggRRggRRggggggggggggggggggggggRR
                                                                            RRggRRggggggggggggggggggggggggggRRggRRggggggggggggggggggggggRRgg
                                                                            RRggRRggggggggggggggggggggggggggRRggRRggggggggggggggggggggggRRgg
                                                                              ggRRgg  ggggggggggggggggggggggRRggRRggggggggggggggggggggggRRgg
                                                                              ggRRgg  gggggggggggggg  ggggggRRggRRggggggggggggggggggggggRRgg
                                                                                RRgg    gggggggggggg  ggggggRRggRRggggggggggggggggggggggRRgggggg      gggg  gg      gggg
                                                                                  gg      gg  gg            RRggRRggggggggggggggggggggggRRggRRgg      gggg  gg      gggg
                                                                                                            RRggRRggggggggggggggggggggggRRggRRggRR  gggggggggg      ggggRR
                                                                                                            RRggRRggggggggggggggggggggggRRggRRggRRggggggggggggggggggggggRR
                                                                                                              ggRRggggggggggggggggggggggRRggRRggRRggggggggggggggggggggggRRgg
                                                                                                                RRgggggggggggggggggg  ggRRggRRggRRggggggggggggggggggggggRRgg
                                                                                                                  gg  gg  gggggggggg    RRggRRggRRggggggggggggggggggggggRRgg
                                                                                                                            gg  gg        ggRRggRRggggggggggggggggggggggRRgg
                                                                                                                            gg            ggRRggRRggggggggggggggggggggggRRgggg
                                                                                                                                          ggRRggRRggggggggggggggggggggggRRggRR
                                                                                                                                            RRggRRggggggggggggggggggggggRRggRRgggg
                                                                                                                                            RRggRRggggggggggggggggggggggRRggRRggRR          gg
                                                                                                                                              ggRRggggggggggggggggggggggRRggRRggRR      gg  gggg
                                                                                                                                                RRgggggggggggggggggg  ggRRggRRggRRgggggggggggggg
                                                                                                                                                  gg  gg  gggggggggg    RRggRRggRRgggggggggggggggggggggg
                                                                                                                                                            gg  gg        ggRRggRRgggggggggggggggggggggggg
                                                                                                                                                            gg            ggRRggRRgggggggggggggggggggggggggg
                                                                                                                                                                          ggRRggRRgggggggggggggggggggggggggggg
                                                                                                                                                                            RRggRRgggggggggggggggggggggggggggg
                                                                                                                                                                            RRggRRgggggggggggggggggggggggggggggggg
                                                                                                                                                                            RRggRRggggggggggggggggggggggggggggRRgg      gggg  gg      gggg
                                                                                                                                                                            RRggRRggggggggggggggggggggggggggggRRggRR  gggggg  gg      gggg
                                                                                                                                                                              ggRRgg  ggggggggggggggggggggggggRRggRRggggggggggggggggggggggRR
                                                                                                                                                                              ggRRgg  gggggggggggggg  ggggggggRRggRRggggggggggggggggggggggRR
                                                                                                                                                                                RRgg    gggggggggggg  ggggggggRRggRRggggggggggggggggggggggRRgg
                                                                                                                                                                                  gg      gg  gg              RRggRRggggggggggggggggggggggRRgg
                                                                                                                                                                                                              RRggRRggggggggggggggggggggggRRgg
                                                                                                                                                                                                              RRggRRggggggggggggggggggggggRRgg
                                                                                                                                                                                                                ggRRggggggggggggggggggggggRRgggg
                                                                                                                                                                                                                  RRgggggggggggggggggg  ggRRggRR
                                                                                                                                                                                                                    gg  gg  gggggggggg    RRggRRgggg
                                                                                                                                                                                                                              gg  gg        ggRRggRR          gg
                                                                                                                                                                                                                              gg            ggRRggRR      gg  gggg
                                                                                                                                                                                                                                            ggRRggRRgggggggggggggg  gggggg
                                                                                                                                                                                                                                              RRggRRgggggggggggggggggggggggg
                                                                                                                                                                                                                                              RRggRRgggggggggggggggggggggggggg
                                                                                                                                                                                                                                              RRggRRgggggggggggggggggggggggggggg
                                                                                                                                                                                                                                              RRggRRggggggggggggggggggggggggggRR
                                                                                                                                                                                                                                                ggRRgg  ggggggggggggggggggggggRRgggg
                                                                                                                                                                                                                                                ggRRgg  gggggggggggggg  ggggggRRggRR          gg
                                                                                                                                                                                                                                                  RRgg    gggggggggggg  ggggggRRggRR      gg  gggg
                                                                                                                                                                                                                                                    gg      gg  gg            RRggRRgggggggggggggg
                                                                                                                                                                                                                                                                              RRggRRgggggggggggggg
                                                                                                                                                                                                                                                                              RRggRRgggggggggggggg
                                                                                                                                                                                                                                                                              RRggRRgggggggggggggg
                                                                                                                                                                                                                                                                              RRggRRgggggggggggggg
                                                                                                                                                                                                                                                                                ggRRgg  gggggggggg
                                                                                                                                                                                                                                                                                ggRRgg  gggggggggg
                                                                                                                                                                                                                                                                                  RRgg    gggggggg
                                                                                                                                                                                                                                                                                    gg      gg
            """;
}

/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.b;

import mage.abilities.Ability;
import mage.abilities.dynamicvalue.common.PermanentsOnBattlefieldCount;
import mage.abilities.effects.OneShotEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.filter.StaticFilters;
import mage.filter.common.FilterLandPermanent;
import mage.filter.predicate.permanent.ControllerPredicate;
import mage.game.Game;
import mage.players.Player;
import mage.target.common.TargetCardInLibrary;

import java.util.UUID;

/**
 *
 * @author North
 */
public class BoundlessRealms extends CardImpl {

    public BoundlessRealms(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.SORCERY},"{6}{G}");

        // Search your library for up to X basic land cards, where X is the number of lands you control, and put them onto the battlefield tapped. Then shuffle your library.
        this.getSpellAbility().addEffect(new BoundlessRealmsEffect());
    }

    public BoundlessRealms(final BoundlessRealms card) {
        super(card);
    }

    @Override
    public BoundlessRealms copy() {
        return new BoundlessRealms(this);
    }
}

class BoundlessRealmsEffect extends OneShotEffect {

    public BoundlessRealmsEffect() {
        super(Outcome.Benefit);
        this.staticText = "Search your library for up to X basic land cards, where X is the number of lands you control, and put them onto the battlefield tapped. Then shuffle your library.";
    }

    public BoundlessRealmsEffect(final BoundlessRealmsEffect effect) {
        super(effect);
    }

    @Override
    public BoundlessRealmsEffect copy() {
        return new BoundlessRealmsEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller == null) {
            return false;
        }
        FilterLandPermanent filter = new FilterLandPermanent();
        filter.add(new ControllerPredicate(TargetController.YOU));

        int amount = new PermanentsOnBattlefieldCount(filter).calculate(game, source, this);
        TargetCardInLibrary target = new TargetCardInLibrary(0, amount, StaticFilters.FILTER_BASIC_LAND_CARD);
        if (controller.searchLibrary(target, game)) {
            controller.moveCards(new CardsImpl(target.getTargets()).getCards(game), Zone.BATTLEFIELD, source, game, true, false, false, null);
        }
        controller.shuffleLibrary(source, game);
        return true;
    }
}
